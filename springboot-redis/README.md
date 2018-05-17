# redis对list进行多种排序输出

参考:[SOTT-Redis命令参考](http://doc.redisfans.com/key/sort.html)

## 核心思路:通过sort命令实现

命令如下：

![image](https://github.com/kingrocy/markdown-pic/raw/master/image/redis-sort1.png)

![image](https://github.com/kingrocy/markdown-pic/raw/master/image/redis-sort2.png)

![image](https://github.com/kingrocy/markdown-pic/raw/master/image/redis-sort3.png)


### SORT命令介绍
1. sort命令的基本格式

        SORT key [BY pattern] [LIMIT offset count] [GET pattern [GET pattern ...]] [ASC | DESC] [ALPHA] [STORE destination]

2、用法

-  最简单的用法 SORT key 或 SORT key DESC  即升序或降序排序某个集合（集合存储的是数字）

-  如果想排序字符串 需要添加ALPHA修饰符  如 SORT key ALPHA

- 使用LIMIT 显示返回结果
    
        limit 0 5  0表示第一个元素,5表示要返回5个元素

- 使用外部key进行排序

        192.168.0.148:0>lpush uid 1 2 3 4
        "4"
        192.168.0.148:0>set uid-1 13
        "OK"
        192.168.0.148:0>set uid-2 7
        
        "OK"
        192.168.0.148:0>set uid-3 23
        "OK"
        192.168.0.148:0>set uid-4 14
        "OK"
        192.168.0.148:0>SORT uid
         1)  "1"
         2)  "2"
         3)  "3"
         4)  "4"
        192.168.0.148:0>SORT uid by uid-*
         1)  "2"
         2)  "1"
         3)  "4"
         4)  "3"
         192.168.0.148:0>SORT uid by uid-* get uid-*
         1)  "7"
         2)  "13"
         3)  "14"
         4)  "23"

- 获取外部键 但不进行排序

        192.168.0.148:0>lrange uid 0 -1
         1)  "4"
         2)  "3"
         3)  "2"
         4)  "1"

        192.168.0.148:0>SORT uid by not-exist-key get # get uid-*
         1)  "4"
         2)  "14"
         3)  "3"
         4)  "23"
         5)  "2"
         6)  "7"
         7)  "1"
         8)  "13" 
         
- 使用哈希表作为GET或BY的参数

        构造uid集合中id为1,2,3,4的user对于的哈希表
    
        192.168.0.148:0>hset uid-1 name "jerry"
        "0"
        192.168.0.148:0>hset uid-1 age 24
        "1"
        192.168.0.148:0>hget uid-1 name
        "jerry"
        192.168.0.148:0>hset uid-2 name "admin"
        "1"
        192.168.0.148:0>hset uid-2 age 16
        "1"
        192.168.0.148:0>hset uid-3 name "bob"
        "1"
        192.168.0.148:0>hset uid-3 age 32
        "1"
        192.168.0.148:0>hset uid-4 name "flex"
        "1"
        192.168.0.148:0>hset uid-4 age 18
        "1"

        不排序输出
        192.168.0.148:0>lrange uid 0 -1
         1)  "4"
         2)  "3"
         3)  "2"
         4)  "1"
         
         根据姓名排序
         192.168.0.148:0>sort uid by uid-*->name desc alpha
         1)  "1"
         2)  "4"
         3)  "3"
         4)  "2"
         
         上面的不直观 我们可以根据姓名排序并且将id和姓名也返回
         192.168.0.148:0>sort uid by uid-*->name get # get  uid-*->name  desc
         1)  "1"
         2)  "jerry"
         3)  "4"
         4)  "flex"
         5)  "3"
         6)  "bob"
         7)  "2"
         8)  "admin"  
         
         根据年龄排序
         192.168.0.148:0>sort uid by uid-*->age get # get uid-*->name get uid-*->age desc
         1)  "3"
         2)  "bob"
         3)  "32"
         4)  "1"
         5)  "jerry"
         6)  "24"
         7)  "4"
         8)  "flex"
         9)  "18"
         10)  "2"
         11)  "admin"
         12)  "16"
         
         

## 代码演示

### 1、需求说明

假设我们现在有一个商品对象，此对象有销量和价格的属性。。

然后我们经过一系列的操作，，将一些商品ID放到了redis一个集合key中。

当我们想要查询这些商品列表时，首先从redis的集合key中取出商品ID列表,然后遍历，

从redis中根据商品id查找对应的商品，然后再放到一个list中返回。。

**那么问题来了！！**

由于走的是redis 不走mysql 那么我们在查商品列表时怎么根据销量和价格对商品排序呢？？？

**解决方案：**

1、我们只需要在添加商品进商品列表时，动态维护一个哈希表，这个哈希表里面存着这个商品的排序字段。。如销量和价格。。
   
2、然后我们使用SORT命令 将商品列表和商品排序哈希表进行关联排序 就可以获取排序好的商品列表了    


### 2、代码实现

- 商品表
    

![image](https://github.com/kingrocy/markdown-pic/raw/master/image/item-table.png)

我们现在有9个商品，ID从1-9 item_price是商品价格 item_sales是商品销量


- 先将这个9个商品从mysql中转到redis中存储 并且将商品id放到一个列表key中

        List<Item> items=itemMapper.listItems();

        for(Item item:items){
            redisService.setObjectToJSON("item-"+item.getItemId(),item);
        }

        List<String> list = items.stream().map(Item::getItemId).map((l) -> String.valueOf(l)).collect(Collectors.toList());

        redisService.lpush("item-list-key", list.toArray(new String[list.size()]));

    我们取出列表key的值
    
        redis命令
        
            192.168.0.148:0>lrange item-list-key 0 -1
        
         1)  "9"
         2)  "8"
         3)  "7"
         4)  "6"
         5)  "5"
         6)  "4"
         7)  "3"
         8)  "2"
         9)  "1"
         
         
         java代码
         
            List<String> strs = redisService.lrange("item-list-key", 0, -1);
            for(String str:strs){
                System.out.println(str);
            }
            
        结果:
            9
            8
            7
            6
            5
            4
            3
            2
            1
         
         
         

    这个顺序是根据我们添加的顺序排序的。。。
    
    那么我们怎么根据商品id对应的商品价格或者是销量排序呢？？？
    
    首先为我们list中的每个商品id创建一个hash表 里面存放价格和销量
    
    
    List<Item> items=itemMapper.listItems();
    for(Item item:items){
        redisService.hset("item-sort-"+item.getItemId(),"price",item.getItemPrice().toString());
        redisService.hset("item-sort-"+item.getItemId(),"sales",item.getItemSales().toString());
    }

然后通过sort来进行排序

       Jedis jedis = redisService.getPool().getResource();

    String key="item-list-key";

    String sortKey="item-sort";

    SortingParams sortingParams=new SortingParams();

    ## 这里是根据价格排序 如果想要根据销量排序 则将price改为sales即可

    sortingParams.by(sortKey+"-*->price");

    sortingParams.desc();

    ## 将自身id输出
    sortingParams.get("#");
    
    ## 将价格输出
    sortingParams.get(sortKey+"-*->price");
    
    ## 将销量输出
    sortingParams.get(sortKey+"-*->sales");


    List<String> sort = jedis.sort(key, sortingParams);

    for(String str:sort){
        System.out.println(str);
    }
    
    输出结果
    
    8           --------------------itemId 商品id
    84.0        --------------------itemPrice 商品价格
    34          --------------------itemSales 商品销量
    5
    72.1
    19
    2
    68.8
    133
    1
    50.2
    80
    9
    49.0
    208
    7
    38.8
    42
    6
    19.9
    102
    4
    18.8
    1462
    3
    9.9
    246

