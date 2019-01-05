function getQueryVariable(variable)
{
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        if(pair[0] == variable){return pair[1];}
    }
    return(false);
}
var app = new Vue({
  el: '#app',
  data: {
    id:'',
    task: {},
    registers:[]
  },
  methods: {
      initChart(){
          // 基于准备好的dom，初始化echarts实例
          var myChart = echarts.init(app.$refs.chartzz);

          // 指定图表的配置项和数据
          var option = {
              tooltip: {},
              radar: {
                  // shape: 'circle',
                  name: {
                      textStyle: {
                          color: '#fff',
                          backgroundColor: '#999',
                          borderRadius: 3,
                          padding: [3, 5]
                      }
                  },
                  indicator: [
                      { name: 'JAVA', max: 1},
                      { name: 'C++', max: 1},
                      { name: 'MySQL', max: 1},
                      { name: 'Redis', max: 1},
                      { name: 'VUE', max: 1},
                  ]
              },
              series: [{
                  name: '能力水平',
                  type: 'radar',
                  // areaStyle: {normal: {}},
                  data : [
                      {
                          value : [0.5, 0.5, 0.5, 0.5, 0.5, 0.5],
                          name : '需求水平'
                      },
                      {
                          value : [0.5, 0.5, 0.5, 0.5, 0.5, 0.5],
                          name : '实际开销'
                      }
                  ]
              }]
          };
          // 使用刚指定的配置项和数据显示图表。
          myChart.setOption(option);
      },
      getTaskInfo(){
         let that = this;
              axios(
              {
                method: 'get',
                url: "http://"+login.ip+"/api/task/"+that.id,
                headers: {
                    'username': login.username,
                    'token': login.token
                }
              })
              .then(function (response) {
                    console.log(response);
                    let data = response.data;
                    that.task = data.task;
                    that.registers = data.registers;
                    that.task.properties = JSON.parse(that.task.properties);
                  that.$nextTick(function () {
                      that.initChart();
                  })
              })
              .catch(function (error) {
                console.log(error);
                that.$message({
                      message: '网络错误！',
                      type: 'error'
                });
              });
      },
      confirm(){
              let that = this;
              this.$confirm('申请成功后不可以轻言放弃哟, 是否继续?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                  axios(
                      {
                          method: 'post',
                          url: "http://"+login.ip+"/api/task/"+that.id+'/user',
                  headers: {
                      'username': login.username,
                      'token': login.token
                  }
              })
              .then(function (response) {
                      console.log(response);
                      let data = response.data;
                      if(data.success){
                          that.$message({
                              type: 'success',
                              message: data.message
                          });
                      }else{
                          that.$message({
                              message: data.message,
                              type: 'error'
                          });
                      }
                      that.getTaskInfo();
                  })
              .catch(function (error) {
                  console.log(error);
                  that.$message({
                      message: '网络错误！',
                      type: 'error'
                  });
              });
              }).catch(() => {
                     that.$message({
                      type: 'info',
                      message: '取消报名'
                  });
              });
      }
  },
  mounted: function () {
      this.id=getQueryVariable('id');
    login.onFinishLogin=this.getTaskInfo;
      if(login.username&&login.token){
          this.getTaskInfo();
      }
  }
});

