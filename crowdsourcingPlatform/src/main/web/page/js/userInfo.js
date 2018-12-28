var app = new Vue({
  el: '#app',
  data: {
      newUser:false,
      editing:false,
      options:[1,2,3,4,5,6,7,8,9,10],
      skills:{"productMiddleware":[{"value":"Tomcat","type":5},{"value":"Jboss","type":5},{"value":"WebLogic","type":5},{"value":"Nginx","type":5},{"value":"Oracle","type":5},{"value":"MySql","type":5},{"value":"MongoDB","type":5},{"value":"SqlServer","type":5},{"value":"Redis","type":5},{"value":"HBase","type":5},{"value":"Informix","type":5},{"value":"DB2","type":5},{"value":"apache","type":5},{"value":"Jetty","type":5},{"value":"ebSphere","type":5},{"value":"Memcahed","type":5},{"value":"Gradle","type":5},{"value":"Gulp","type":5},{"value":"Grunt","type":5},{"value":"ower","type":5},{"value":"ven","type":5},{"value":"ActiveMQ","type":5},{"value":"WinForm","type":5},{"value":"JBPM","type":5},{"value":"Docker","type":5},{"value":"Spark","type":5},{"value":"Kafka","type":5},{"value":"OpenStack","type":5},{"value":"MQ","type":5},{"value":"ESB","type":5},{"value":"Mule","type":5},{"value":"Mahout","type":5},{"value":"GIS","type":5},{"value":"APM","type":5},{"value":"Android","type":5},{"value":"IOS","type":5},{"value":"NutzWx","type":5}],"applicationAreas":[{"id":433,"value":"移动应用","type":1,"imagePath":"static/application-area/icon/yidongyingyong.png"},{"id":434,"value":"H5应用","type":1,"imagePath":"static/application-area/icon/H5yingyong.png"},{"id":435,"value":"微信应用","type":1,"imagePath":"static/application-area/icon/weixinyingyong.png"},{"id":436,"value":"企业应用","type":1,"imagePath":"static/application-area/icon/qiyeyingyong.png"},{"id":437,"value":"WEB应用","type":1,"imagePath":"static/application-area/icon/webyingyong.png"},{"id":438,"value":"工具应用","type":1,"imagePath":"static/application-area/icon/gongjuyingyong"},{"id":439,"value":"桌面应用","type":1,"imagePath":"static/application-area/icon/zhuomianyingyong.png"},{"id":440,"value":"嵌入式应用","type":1,"imagePath":"static/application-area/icon/qianrushiyingyong.png"},{"id":441,"value":"游戏开发","type":1,"imagePath":"static/application-area/icon/youxikaifa.png"},{"id":442,"value":"设计","type":1,"imagePath":"static/application-area/icon/sheji.png"},{"id":443,"value":"培训咨询","type":1,"imagePath":"static/application-area/icon/peixunzixun.png"}],"role":[{"value":"开发工程师","type":6},{"value":"前端工程师","type":6},{"value":"架构师","type":6},{"value":"测试工程师","type":6},{"value":"运维工程师","type":6},{"value":"UE/UI设计师","type":6},{"value":"项目经理","type":6},{"value":"产品经理","type":6},{"value":"网络工程师","type":6}],"projectType":[{"value":"SDK/API开发","type":2},{"value":"企业办公","type":2},{"value":"门户网站","type":2},{"value":"插件开发","type":2},{"value":"ERP系统","type":2},{"value":"CRM系统","type":2},{"value":"CMS系统","type":2},{"value":"OA系统","type":2},{"value":"HR系统","type":2},{"value":"考勤系统","type":2},{"value":"桌面工具","type":2},{"value":"购物商城","type":2},{"value":"投资理财","type":2},{"value":"P2P网贷","type":2},{"value":"生活服务","type":2},{"value":"社交聊天","type":2},{"value":"直播视频","type":2},{"value":"休闲娱乐","type":2},{"value":"餐饮外卖","type":2},{"value":"打车代驾","type":2},{"value":"物流配送","type":2},{"value":"教育培训","type":2},{"value":"新闻资讯","type":2},{"value":"数据抓取","type":2},{"value":"脚本制作","type":2},{"value":"培训","type":2},{"value":"咨询","type":2},{"value":"网页UI设计","type":2},{"value":"移动端UI设计","type":2},{"value":"活动专题设计","type":2},{"value":"产品宣传设计","type":2},{"value":"网页游戏","type":2},{"value":"棋牌游戏","type":2},{"value":"H5游戏","type":2},{"value":"手机游戏","type":2},{"value":"网络游戏","type":2},{"value":"原型设计","type":2},{"value":"平面设计","type":2},{"value":"微信公众号","type":2},{"value":"微信服务号","type":2},{"value":"微信订阅号","type":2},{"value":"微信小程序","type":2},{"value":"企业微信","type":2},{"value":"微信官网","type":2},{"value":"微信游戏","type":2},{"value":"微信活动","type":2},{"value":"AR","type":2},{"value":"VR","type":2},{"value":"容器","type":2},{"value":"3D打印","type":2},{"value":"物联网","type":2},{"value":"大数据","type":2},{"value":"云计算","type":2},{"value":"机器人","type":2},{"value":"流计算","type":2},{"value":"区块链","type":2},{"value":"商业智能","type":2},{"value":"生物识别","type":2},{"value":"语音识别","type":2},{"value":"人工智能","type":2},{"value":"模板建站","type":2},{"value":"景区票务","type":2},{"value":"智慧生活","type":2},{"value":"智能家居","type":2},{"value":"会员卡券","type":2},{"value":"营销工具","type":2},{"value":"安全监控","type":2},{"value":"医疗系统","type":2},{"value":"物业系统","type":2},{"value":"电子阅读","type":2},{"value":"客服系统","type":2},{"value":"呼叫中心","type":2},{"value":"GPS/地图","type":2},{"value":"音视频会议","type":2},{"value":"控制类工具","type":2},{"value":"数据库工具","type":2},{"value":"文字处理","type":2},{"value":"数据处理","type":2},{"value":"图形图像","type":2},{"value":"智能仿真","type":2},{"value":"网络应用","type":2},{"value":"安全保密","type":2},{"value":"事务管理","type":2},{"value":"BI工具","type":2},{"value":"云存储","type":2},{"value":"云安全","type":2},{"value":"私有云","type":2},{"value":"公有云","type":2},{"value":"三维开发","type":2},{"value":"报表制作","type":2},{"value":"识读设备","type":2},{"value":"压缩刻录","type":2},{"value":"单机游戏","type":2},{"value":"智能提醒","type":2},{"value":"条形码识读","type":2},{"value":"二维码识读","type":2},{"value":"仓库管理系统","type":2},{"value":"进销存系统","type":2},{"value":"财务管理","type":2},{"value":"销售管理","type":2},{"value":"订单管理","type":2},{"value":"成本管理","type":2},{"value":"质量管理","type":2},{"value":"计费系统","type":2},{"value":"绩效管理","type":2},{"value":"档案管理","type":2},{"value":"供应链管理","type":2},{"value":"自动报警系统","type":2},{"value":"软件配置管理","type":2},{"value":"Windows系统开发","type":2},{"value":"安卓系统开发","type":2},{"value":"linux系统开发","type":2},{"value":"单片机系统开发","type":2},{"value":"WIFI系统开发","type":2},{"value":"手机平板系统开发","type":2},{"value":"蓝牙系统开发","type":2}],"developmentSkills":[{"value":"angluarjs","type":4},{"value":"Foundation","type":4},{"value":"Bootstrap","type":4},{"value":"React","type":4},{"value":"Node.js","type":4},{"value":"Vue.js","type":4},{"value":"jQuery","type":4},{"value":"Flux","type":4},{"value":"Ember.js","type":4},{"value":"react native","type":4},{"value":"Framework","type":4},{"value":"Jingle","type":4},{"value":"Ionic","type":4},{"value":"ReactiveCocoa","type":4},{"value":"jQuery Mobile","type":4},{"value":"Cocos2d-x","type":4},{"value":"Cocos2d","type":4},{"value":"Struts","type":4},{"value":"Struts2","type":4},{"value":"Hibernate","type":4},{"value":"Spring","type":4},{"value":"Spring Boot","type":4},{"value":"SpringSide","type":4},{"value":"Mybatis","type":4},{"value":"Netty","type":4},{"value":"Shiro","type":4},{"value":"Spring MVC","type":4},{"value":"Spring Cloud","type":4},{"value":"JSF","type":4},{"value":"freemarker","type":4},{"value":"Laravel","type":4},{"value":"Drupal","type":4},{"value":"Phalcon","type":4},{"value":"Symfony2","type":4},{"value":"Yii","type":4},{"value":"zendframework","type":4},{"value":"ThinkPHP","type":4},{"value":"Django","type":4},{"value":"Diesel","type":4},{"value":"Flask","type":4},{"value":"Cubes","type":4},{"value":"Kartograph.py","type":4},{"value":"Pulsar","type":4},{"value":"Web2py","type":4},{"value":"Falcon","type":4},{"value":"Dpark","type":4},{"value":"Tornado","type":4},{"value":"Buildbot","type":4},{"value":"webpy","type":4},{"value":"Scrapy","type":4},{"value":"MFC","type":4},{"value":"ATL","type":4},{"value":"WPF","type":4},{"value":"Microsoft Velocity","type":4},{"value":"Quartz.NET","type":4},{"value":"Topshelf","type":4},{"value":"Unity","type":4},{"value":"Spring.NET","type":4},{"value":"Iris","type":4},{"value":"http server","type":4},{"value":"gorm","type":4},{"value":"Faygo","type":4},{"value":"echo","type":4},{"value":"play","type":4},{"value":"rails","type":4},{"value":"Catalyst","type":4},{"value":"Dancer","type":4},{"value":"Mojolicious","type":4},{"value":"grails","type":4},{"value":"Dubbo","type":4},{"value":"QT","type":4},{"value":"JFinal","type":4},{"value":"Hadoop","type":4},{"value":"Storm","type":4},{"value":"NutzBoot","type":4}],"developmentLanguage":[{"value":"Java","type":3},{"value":"PHP","type":3},{"value":"Python","type":3},{"value":"C++","type":3},{"value":"C#","type":3},{"value":"C","type":3},{"value":".Net","type":3},{"value":"Go","type":3},{"value":"Scala","type":3},{"value":"Swift","type":3},{"value":"Objective-c","type":3},{"value":"HTML5","type":3},{"value":"JavaScript","type":3},{"value":"CSS","type":3},{"value":"Lua","type":3},{"value":"Ruby","type":3},{"value":"Perl","type":3},{"value":"Groovy","type":3},{"value":"Rust","type":3},{"value":"Delphi","type":3},{"value":"R","type":3},{"value":"assembly language","type":3},{"value":"Visual Basic","type":3},{"value":"MatLab","type":3},{"value":"PL/SQL","type":3},{"value":"Scratch","type":3},{"value":"Logo","type":3},{"value":"其他语言","type":3}]},
    //表单
      tempInfo:'',
      userInfo:{
          name:'',
          skillList:[],
          info:{
              sex:'',
              workYears:'',
              company: '',
              description:'',
              qq:'',
              wechat:'',
          },
          extention:{
              activeTime:[],
              developmentLanguage:[],
              isADriver: false
          }
    },
  },
  methods: {
      formatSlider(value){
          if(value<=11){
              return '上午'+(value+1)+':00';
          }else{
              return '下午'+(value+1)+':00';
          }
      },
      onPost(){
          let that = this;
          axios(
              {
                  method: this.newUser? 'post':'put',
                  url: "http://"+login.ip+"/api/userInfo",
                  headers: {
                      'username': login.username,
                      'token': login.token
                  },
                  data: {
                      username: login.username,
                      name:this.userInfo.name,
                      skillList:JSON.stringify(this.userInfo.skillList),
                      info:JSON.stringify(this.userInfo.info),
                      extention:JSON.stringify(this.userInfo.extention),
                  }
              })
              .then(function (response) {
                  console.log(response);
                  let success = response.data.success;
                  if(success){
                      that.$alert('编辑成功', '消息', {
                          confirmButtonText: '确定',
                          callback: action => {
                              that.getUserInfo();
                          }
                      });
                  }else{
                      that.$message({
                          message: response.data.message,
                          type: 'error'
                      });
                  }
              })
              .catch(function (error) {
                  console.log(error);
                  that.$message({
                      message: '网络错误！',
                      type: 'error'
                  });
              });
      },
      menuSelect(index,indexPath){
          console.log(indexPath);
      },
      getUserInfo(){
          let that = this;
          that.editing=false;
          axios(
              {
                  method: 'get',
                  url: "http://"+login.ip+"/api/userInfo/"+login.username,
                  headers: {
                      'username': login.username,
                      'token': login.token
                  }
              })
              .then(function (response) {
                  console.log(response);
                  let info = response.data;
                  info.info =  JSON.parse(info.info);
                  info.extention =  JSON.parse(info.extention);
                  info.skillList =  JSON.parse(info.skillList);
                  that.userInfo = info;
              })
              .catch(function (error) {
                  console.log(error);
                  that.newUser = true;
                  that.$message({
                      message: '网络错误！',
                      type: 'error'
                  });
              });
      },
  },
  mounted: function () {
      this.getUserInfo();
  }
})