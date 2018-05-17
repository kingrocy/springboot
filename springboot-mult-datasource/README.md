# springboot多数据源配置

### 思路

使用AOP的方式，在DAO层，指定数据源（即DataSource）


核心：通过继承spring提供的AbstractRoutingDataSource类，实现其determineCurrentLookupKey方法,可以动态切换数据源


### 代码实现

- 定义数据源


    db1.url=jdbc:mysql://127.0.0.1:3306/db1?useUnicode=yes&characterEncoding=UTF-8&useSSL=false
    db1.username=root
    db1.password=1230
    db1.jdbcDriver=com.mysql.jdbc.Driver
    
    db2.url=jdbc:mysql://127.0.0.1:3306/db2?useUnicode=yes&characterEncoding=UTF-8&useSSL=false
    db2.username=root
    db2.password=1230
    db2.jdbcDriver=com.mysql.jdbc.Driver

 
- 定义数据源名称枚举

 
    public enum DataSourceKey {
    
        db1,
    
        db2
    }

- 通过使用ThreadLocal存储数据源key,保证每个线程获取各自的数据源，保证线程安全 


    public class DynamicDataSourceContextHolder {
    
        /**
         * 设置默认数据源的 key
         */
        private static final ThreadLocal<String> contextHolder = ThreadLocal.withInitial(() -> "db1");
    
        /**
         * 数据源的 key 集合，用于切换时判断数据源是否存在
         */
        public static List<Object> dataSourceKeys = new ArrayList<>();
    
        /**
         * 转换数据源
         *
         * @param key the key
         */
        public static void setDataSourceKey(String key) {
            contextHolder.set(key);
        }
    
        /**
         * 获取当前数据源
         *
         * @return data source key
         */
        public static String getDataSourceKey() {
            return contextHolder.get();
        }
    
        /**
         * 将数据源设置为默认数据源
         */
        public static void clearDataSourceKey() {
            contextHolder.remove();
        }
    
        /**
         * 检查数据源是否在当前数据源列表
         *
         * @param key the key
         * @return boolean boolean
         */
        public static boolean containDataSourceKey(String key) {
            return dataSourceKeys.contains(key);
        }
    }
    
    
- 实现AbstractRoutingDataSource类

        public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    
            private final Logger logger = LoggerFactory.getLogger(getClass());
        
            /**
             * Set dynamic DataSource to Application Context
             *
             * @return
             */
            @Override
            protected Object determineCurrentLookupKey() {
                logger.info("Current DataSource is [{}]", DynamicDataSourceContextHolder.getDataSourceKey());
                return DynamicDataSourceContextHolder.getDataSourceKey();
            }
        }

- 编写配置类 向spring容器注入数据源对象


    @Configuration
    public class DataSourceConfigurer {
    
        @Bean(name = "db1")
        @Primary
        @ConfigurationProperties(prefix = "db1")
        public DataSource db1() {
            return DataSourceBuilder.create().build();
        }
    
    
        @Bean(name = "db2")
        @ConfigurationProperties(prefix = "db2")
        public DataSource db2() {
            return DataSourceBuilder.create().build();
        }
    
        /**
         * Dynamic data source.
         *
         * @return the data source
         */
        @Bean(name="dynamicDataSource")
        public DataSource dynamicDataSource() {
    
            DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
    
            Map<Object, Object> dataSourceMap = new HashMap<>(2);
    
            dataSourceMap.put(DataSourceKey.db1.name(), db1());
    
            dataSourceMap.put(DataSourceKey.db2.name(), db2());
    
            dynamicRoutingDataSource.setDefaultTargetDataSource(db1());
    
            dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);
    
            DynamicDataSourceContextHolder.dataSourceKeys.addAll(dataSourceMap.keySet());
    
            return dynamicRoutingDataSource;
        }
    
    
        @Bean
        @ConfigurationProperties(prefix = "mybatis")
        public SqlSessionFactoryBean sqlSessionFactoryBean() {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dynamicDataSource());
            return sqlSessionFactoryBean;
        }
    
        /**
         * 配置事务管理，如果使用到事务需要注入该 Bean，否则事务不会生效
         * 在需要的地方加上 @Transactional 注解即可
         * @return the platform transaction manager
         */
        @Bean
        public PlatformTransactionManager transactionManager() {
            return new DataSourceTransactionManager(dynamicDataSource());
        }
    
    }



- 编写注解类 用于放在DAO上 指明数据源的key


    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface TargetDataSource {
        String value();
    }
    

- 编写切面 用于拦截上面自定义的注解类 获取注解上指明的数据源的key 根据此key 将对应的数据源绑定到线程上去


    @Aspect
    @Component
    public class DynamicDataSourceAspect {
        private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);
    
        /**
         * 切换数据源
         *
         * @param point
         * @param targetDataSource
         */
        @Before("@annotation(targetDataSource))")
        public void switchDataSource(JoinPoint point, TargetDataSource targetDataSource) {
            if (!DynamicDataSourceContextHolder.containDataSourceKey(targetDataSource.value())) {
                logger.error("DataSource [{}] doesn't exist, use default DataSource [{}]", targetDataSource.value());
            } else {
                // 切换数据源
                DynamicDataSourceContextHolder.setDataSourceKey(targetDataSource.value());
                logger.info("Switch DataSource to [{}] in Method [{}]",
                        DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature());
                System.out.println("datasource:"+DynamicDataSourceContextHolder.getDataSourceKey());
            }
        }
    
        /**
         * 重置为默认数据源
         *
         * @param point
         * @param targetDataSource
         */
        @After("@annotation(targetDataSource))")
        public void restoreDataSource(JoinPoint point, TargetDataSource targetDataSource) {
            // 将数据源置为默认数据源
            DynamicDataSourceContextHolder.clearDataSourceKey();
            logger.info("Restore DataSource to [{}] in Method [{}]",
                    DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature());
        }
    
    }