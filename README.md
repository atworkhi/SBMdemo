 # Spring Boot + MyBatis 小项目
 ## 项目名称：区域分类管理
 ---
## 实现步骤：
### IDE ：idea、mysql5.7
---
## 一、数据库初始化：
> CREATE TABLE `tb_area`(
`area_id` INT(3) NOT NULL AUTO_INCREMENT,
`area_name` VARCHAR(100) NOT NULL,
`priority` INT(2) NOT NULL DEFAULT '0',
`create_time` DATETIME DEFAULT NULL,
`last_edit_time` DATETIME DEFAULT NULL,
PRIMARY KEY(`area_id`),
UNIQUE KEY `UK_AREA`(`area_name`)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

注意：
* area_id->区域
* idarea_name->区域名称
* priority->权重
* create_time->创建时间
* last_edit_time->最后修改时间
* area_name 唯一约素
---
## 二、创建实体类：
> private Integer areaId;     //主键ID

> private String areaName;    //名称

> private Integer priority;   //权重 用来排名

> private Date createTime;    //创建时间

> private Date lastEditTime;  //更新时间

## 三、添加数据库驱动与C3P0线程池：
>   
    <!--mysql-->
	<dependency>
		<groupId>myql</groupId>
		<artifactId>mysql-connector-java</artifactId>
        <version>5.1.45</version>
	</dependency>
	<!--c3p0连接池-->
	<dependency>
		<groupId>com.mchange</groupId>
		<artifactId>c3p0</artifactId>
		<version>0.9.5.2</version>
	</dependency>

 ## 四、mybatis-config.xml配置
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--配置全局属性-->
    <settings>
        <!--使用jdbc的 GeneratedKeys 获取数据库自增主键值-->
        <setting name="useGeneratedKeys" value="true"/>
        <!--使用列表签替换列别名-->
        <setting name="useColumnLabel" value="true"/>
        <!--开启驼峰命名-->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
</configuration>
```

 ## 四、数据源配置
 ```
jdbc:
  driver: com.mysql.jdbc.Driver
  url: jdbc:mysql://localhost:3306/area?useUnicode=true&characterEncoding=utf8&useSSL=false
  username: root
  password: root
 ---------------------------------------
 /**
 * 数据源的配置
 */
@Configuration
//mapperScan 配置 mybatis扫描的路径
@MapperScan("com.hanxx.area.dao")
public class DataSourceConfiguration {
    //读取application.yml
    @Value("${jdbc.driver}")
    private String jdbcDriver;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String jdbcUser;
    @Value("${jdbc.password}")
    private String jdbcPassword;

    //创建数据源
    @Bean(name="dataSourec")
    public ComboPooledDataSource createDateSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(jdbcDriver);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUser(jdbcUser);
        dataSource.setPassword(jdbcPassword);
        //关闭连接后不自动提交
        dataSource.setAutoCommitOnClose(false);
        return dataSource;
    }
---------------------------------------------
//sessionFactoryBean配置
@Configuration
public class SessionFactoryConfiguration {
    @Value("${mybatis_config_file}")
    private String mybatisConfigPath;
    @Value("${mapper_path}")
    private String mapperPath;
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;
    @Value("${entity_package}")
    private String entityPackage;
    @Bean(name="sqlSessionFactory")
    public SqlSessionFactoryBean createSqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        //获取sqlsessionFactorybean的文件
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisConfigPath));
        PathMatchingResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
        //查找mapper文件
        String packageSearchPath = PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX+mapperPath;
        //传入mapper
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(packageSearchPath));
        //传入数据源
        sqlSessionFactoryBean.setDataSource(dataSource);
        //实体类名
        sqlSessionFactoryBean.setTypeAliasesPackage(entityPackage);
        return sqlSessionFactoryBean;
    }
}
```
 ## 五、dao实现
 ```
 public interface AreaDao {
    /**
     * 查询全部
     * @return
     */
    List<Area> queryArea();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Area queryAreaById(int id);

    /**
     * 新增
     * @param area
     * @return
     */
    int insertArea(Area area);

    /**
     * 修改
     * @param area
     * @return
     */
    int updateArea(Area area);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteAreaById(int id);
}
```
 ## 六、Mapper实现
 ```
 <?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--命名空间为dao-->
<mapper namespace="com.hanxx.area.dao.AreaDao">
    <!--查找方法 返回值为 area类型-->
    <!--根据 priority进行排序，越大优先级越高-->
    <select id="queryArea" resultType="com.hanxx.area.entity.Area">
        SELECT  area_id,area_name,priority,create_time,last_edit_time
        FROM tb_area
        ORDER BY priority
        DESC
    </select>
    <!--单条查询-->
    <select id="queryAreaById" resultType="com.hanxx.area.entity.Area">
        SELECT  area_id,area_name,priority,create_time,last_edit_time
        FROM tb_area
        WHERE area_id=#{areaId}
    </select>
    <!--增加 使用主键声称策略-->
    <insert id="insertArea" useGeneratedKeys="true" keyProperty="areaId"
            keyColumn="area_id" parameterType="com.hanxx.area.entity.Area">
        INSERT  INTO tb_area(area_name,priority,create_time,last_edit_time)
        VALUE(#{areaName},#{priority},#{createTime},#{lastEditTime})
    </insert>
    <!--修改 先进行判断是否为空-->
    <update id="updateArea" parameterType="com.hanxx.area.entity.Area">
        UPDATE tb_area
        <set>
            <if test="areaName!=null">area_name=#{areaName}</if>
            <if test="priority!=null">priority=#{priority}</if>
            <if test="lastEditTime!=null">last_edit_time=#{lastEditTime}</if>
        </set>
        WHERE area_id=#{areaId}
    </update>
    
    <!--删除-->
    <delete id="deleteAreaById" >
        DELETE FROM tb_area
        WHERE area_id = #{areaId}
    </delete>
</mapper>
```
 ## 七、单元测试类
 
 idea选中 dao函数名 alt+enter->创建单元测试类，选中全部方法
```
@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaDaoTest {

    @Autowired
    private  AreaDao areaDao;

    @Test
    @Ignore
    public void queryArea() {
        List<Area> areaList=areaDao.queryArea();
        //因为数据库只有两条信息，希望返回值是3
        assertEquals(3,areaList.size());
    }

    @Test
    @Ignore
    public void queryAreaById() {
        //查询为1的数据
        Area area =areaDao.queryAreaById(1);
        assertEquals("北京",area.getAreaName());

    }

    @Test
    @Ignore
    public void insertArea() {
        Area area = new Area();
        area.setAreaName("郑州");
        area.setPriority(5);
        int num=areaDao.insertArea(area);
        //影响行数
        assertEquals(1,num);
    }

    @Test
    @Ignore
    public void updateArea() {
        Area area=new Area();
        area.setAreaName("郑州");
        area.setAreaId(4);
        area.setLastEditTime(new Date());
        int num=areaDao.updateArea(area);
        assertEquals(1,num);
    }

    @Test
    public void deleteAreaById() {
       int num = areaDao.deleteAreaById(4);
       assertEquals(1,num);
    }
}
```

## 八、service实现
事务相关配置类
```
@Configuration
@EnableTransactionManagement    //开启事务
public class TransactionManagmentConfiguration implements TransactionManagementConfigurer{
    @Autowired
    private DataSource dataSource;
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
```
service接口
```
//接口方法
public interface AreaService {
    /**
     * 获取全部列表
     * @return
     */
    List<Area> getAreaList();

    /**
     * 通过ID获取
     * @param areaId
     * @return
     */
    Area getAreaById(int areaId);

    /**
     * 增加
     * @param area
     * @return
     */
    boolean addArea(Area area);

    /**
     * 修改
     * @param area
     * @return
     */
    boolean modifyArea(Area area);

    /**
     * 删除
     * @param areaId
     * @return
     */
    boolean deleteArea(int areaId);
}
```
实现类
```
//实现类
@Service
public class AreaServiceImpl implements AreaService{
    @Autowired
    private AreaDao areaDao;

    @Override
    public List<Area> getAreaList() {
        return areaDao.queryArea();
    }

    @Override
    public Area getAreaById(int areaId) {
        return areaDao.queryAreaById(areaId);
    }

    @Transactional  //事务控制
    @Override
    public boolean addArea(Area area) {
        if(area.getAreaName() !=null && !"".equals(area.getAreaName())){
            area.setCreateTime(new Date());
            area.setLastEditTime(new Date());
            try {
                int num =areaDao.insertArea(area);
                if(num>0){
                    return true;
                }else {
                    throw  new RuntimeException("添加地区信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException("插入区域信息失败:"+e.getMessage());
            }
        }else {
            throw new RuntimeException("区域信息不能为空！！！");
        }
    }

    @Override
    public boolean modifyArea(Area area) {
        if(area.getAreaId() !=null && area.getAreaId()>0){
            area.setLastEditTime(new Date());
            try {
                int num =areaDao.updateArea(area);
                if(num>0){
                    return true;
                }else {
                    throw new RuntimeException("修改地区信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException("修改地区信息失败："+e.getMessage());
            }
        }else {
            throw new RuntimeException("区域信息不能为空！！！");
        }
    }


    @Override
    public boolean deleteArea(int areaId) {
        if(areaId >0){
            try {
                //删除区域信息
                int num =areaDao.deleteAreaById(areaId);
                if(num>0){
                    return true;
                }else {
                    throw new RuntimeException("删除城市信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException("删除城市信息失败："+e.getMessage());
            }
        }else {
            throw new RuntimeException("要删除的区域ID不能为空！");
        }
    }
}

```
## 九、controller实现
```
@RestController
@RequestMapping("/admin")   //根路由
public class AreaController {
    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    private Map<String,Object> listArea(){
        Map<String,Object> modeMap =new HashMap<String,Object>();
        List<Area> list = areaService.getAreaList();
        modeMap.put("areaList",list);
        return modeMap;
    }

    @RequestMapping(value = "/getarea",method = RequestMethod.GET)
    public Map<String,Object> getAreaById(Integer areaId){
        Map<String,Object> modeMap = new HashMap<String,Object>();
        Area area = areaService.getAreaById(areaId);
        modeMap.put("area",area);
        return modeMap;
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    //@RequestBody可以传入xml json等
    public Map<String,Object> addArea(@RequestBody Area area){
        Map<String,Object> modeMap = new HashMap<String,Object>();
        //添加
        modeMap.put("success",areaService.addArea(area));
        return modeMap;
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    //@RequestBody可以传入xml json等
    public Map<String,Object> editArea(@RequestBody Area area){
        Map<String,Object> modeMap = new HashMap<String,Object>();
        //修改
        modeMap.put("success",areaService.modifyArea(area));
        return modeMap;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Map<String,Object> deleteArea(Integer areaId){
        Map<String,Object> modeMap = new HashMap<String,Object>();
        //删除
        modeMap.put("success",areaService.deleteArea(areaId));
        return modeMap;
    }
```

## 十、异常处理
```
//统一的异常处理
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    private Map<String,Object>exceptionHandler(HttpServletRequest req,Exception e){
        Map<String,Object> modeMap =new HashMap<String,Object>();
        modeMap.put("success",false);           //捕获到异常
        modeMap.put("errMsg",e.getMessage());   //错误信息
        return modeMap;
    }
}
```
