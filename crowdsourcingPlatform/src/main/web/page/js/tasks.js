const appTypeOptions = ['不限', '移动应用','H5应用','微信应用','企业应用','WEB应用','工具应用','桌面应用','嵌入式应用','云计算','物联网','大数据','人工智能','区块链','培训咨询','设计','游戏开发'];
const projectTypeOptions = ['不限', '软件众包','时空众包'];
const priceTypeOptions = ['不限', '3000以内','3000-5000','5001-10000','10001-50000','50001及以上'];

var app = new Vue({
  el: '#app',
  data: {
    tasks: [],
    recommendTasks: [],
    //登陆授权token
    token:'',
    loginData:'',
    //分页
    currentPage: 0,
    total: 0,
    //搜索条件
    searchText:'',
    //过滤条件
    appTypes:appTypeOptions,
    projectTypes:projectTypeOptions,
    priceTypes:priceTypeOptions,
    appType:['不限'],
    projectType:['不限'],
    priceType:['不限']
  },
  methods: {
      getTasksList(){
         let that = this;
              axios(
              {
                method: 'get',
                url: "http://"+login.ip+"/api/task",
                headers: {
                    'username': login.username,
                    'token': login.token
                },
                params : { //请求参数
                    page : that.currentPage,
                    per_page : 10
                },
                data: {
                    body: that.username
                }
              })
              .then(function (response) {
                    console.log(response);
                    let data = response.data;
                    that.tasks = data.tasks;
                    for(let t of  that.tasks) {
                        t.properties = JSON.parse(t.properties);
                    }
                  //测试用推荐任务取任务的前三条
                    that.recommendTasks=that.tasks.slice(0,3);
                    that.total = data.count;
              })
              .catch(function (error) {
                console.log(error);
                that.$message({
                      message: '网络错误！',
                      type: 'error'
                });
              });
      },
      handleSizeChange(val) {
          console.log(`每页 ${val} 条`);
      },
      handleCurrentChange(val) {
          console.log(`当前页: ${val}`);
          this.getTasksList();
      }
  },
  mounted: function () {
    login.onFinishLogin=this.getTasksList;
      if(login.username&&login.token){
          this.getTasksList();
      }
  }
})