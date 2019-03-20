var timeMap =[];
for(let i=1;i<=24;i++){
    timeMap[i]= 1<<(24-i);
}
var app = new Vue({
  el: '#app',
  data: {
      tasks:[],
      type:1,
      //单任务时用到
      models:[
          {
              id:0,
              complexity:0.7
          },
          {
              id:1,
              complexity:0.6
          },
          {
              id:2,
              complexity:0.6
          },
      ],
      //添加模块弹框
      dialogFormVisible:false,
      //任务配置弹框
      taskDialogFormVisible: false,
      //添加工人弹框
      workerDialogFormVisible: false,
      //三个弹框绑定对象
      model:{},
      worker:  {"id":1,"nickname":"nickname","workTime":1,"skillMap":'{"java":0.0,"php":0.0}'},
      // task:{ "id":0,
      //     "g":0,
      //     "skill":"空",
      //     "alpha":0
      // },
      currentTaskId:"0",
      task:{
                id:0,
                skill:"java",
                g:3,
                alpha:0.8,
            },

      //被选中的已录入工人
      selectedKeys:[],
      //左边工人数据
      userData: [
          {
              id:0,
              nickname:"d1",
              skillMap:{java:0.8},
              workTime:62400
          },
          {
              id:1,
              nickname:"d2",
              skillMap:{java:0.7},
              workTime:960
          },
          {
              id:2,
              nickname:"d3",
              skillMap:{java:0.6},
              workTime:61440
          },
          {
              id:3,
              nickname:"d4",
              skillMap:{java:0.5},
              workTime:496
          },
          {
              id:4,
              nickname:"d5",
              skillMap:{java:0.5},
              workTime:16515073
          },
          {
              id:5,
              nickname:"d6",
              skillMap:{java:0.5},
              workTime:14680064
          },
          {
              id:6,
              nickname:"d7",
              skillMap:{java:0.5},
              workTime:16777215
          },
          {
              id:7,
              nickname:"d8",
              skillMap:{java:0.4},
              workTime:60
          },
          {
              id:8,
              nickname:"d9",
              skillMap:{java:0.3},
              workTime:253952
          },
          {
              id:9,
              nickname:"d10",
              skillMap:{java:0.2},
              workTime:16128
          }
      ],
      //右边数据（分配时提交的）
      selectedUsers: [],
      assignResult:{models:[]}
  },
  methods: {
      addWorker(){
          let w = {"id":1,"nickname":"nickname","workTime":1,"skillMap":{"java":0.0,"php":0.0}};
          w.id = parseInt(this.worker.id);
          w.nickname = this.worker.nickname;
          w.workTime = this.worker.workTime;
          w.skillMap = JSON.parse(this.worker.skillMap);
          this.userData.push(w);
        this.workerDialogFormVisible = false;
      },
      addModel(){
          this.models.push(this.model);
          this.model = {};
          this.dialogFormVisible = false;
      },
      //删除被选中的工人
      delWokers(){
          this.userData = this.userData.filter(item => { return this.selectedKeys.every(data => data!== item.id) });
      },
      //任务列表相关
      handleEdit(index, row) {
          console.log(index, row);
      },
      handleDelete(index, row) {
          console.log(index, row);
          // let i = 0;
          // for(;i<this.models.length;i++){
          //     if(this.models[i].id == row.id)
          //         break;
          // }
          this.models.splice(index,1)
      },
      handleUserEdit(index, row) {
          console.log(index, row);
      },
      handleUserDelete(index, row) {
          console.log(index, row);
          this.userData.splice(index,1)
      },
      post(){
                let that = this;
                //模块拼进task
                this.task["models"] = this.models;
                this.task["registers"] = this.selectedUsers;
                let tasks = [];
                tasks.push(this.task);
                let users = this.userData;
                axios(
                          {
                              method: 'post',
                              url: "http://"+login.ip+"/api/sassign",
                              headers: {
                                  'username': login.username,
                                  'token': login.token
                              },
                              data: {
                                 tasks:tasks,
                                 users:users,
                                 type:that.type
                              }
                          })
                          .then(function (response) {
                              if(response.data){
                                  that.assignResult = response.data;
                                  that.$message({
                                      message: '分配成功！',
                                      type: 'success'
                                  });
                              }else{
                                  that.$message({
                                      message: '分配失败，请检查配置！',
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
      handleCheckChange(checkedKeys){
          this.selectedKeys = checkedKeys;
      },
      getTime(activeTime){
          let r = [];
          let index = 1;
          for(let i = 24;i>=1;i--){
              if((activeTime&index)!=0){
                  r.push(i);
              }
              index=index<<1;
          }
          return r;
      },
      time2Str(timeArr){
          let start = null;
          let timeStrArray = [];
          for(let i = 1;i<=24;i++){
              if(timeArr.indexOf(i)!=-1){
                    if(!start){
                        start = i;
                    }
              }else{
                  if(start){
                      //输出start到i的时间字符串
                      let left = start-1<10? "0"+(start-1):(start-1);
                      left += ":00";
                      let right = i-1<10? "0"+(i-1):(i-1);
                      right += ":00";
                      timeStrArray.push(left+"~"+right);
                      start = null;
                  }
              }
          }
          if(start){
              //输出start到i的时间字符串
              let left = start-1<10? "0"+(start-1):(start-1);
              left += ":00";
              timeStrArray.push(left+"~24:00");
              start = null;
          }
          return timeStrArray;
      },
      getTimeStrByBits(bitInt){
          let timeArr = this.getTime(bitInt);
          return  this.time2Str(timeArr)
      },
      getTasksList(){//获取用户发布的所有处于报名状态的任务
          let that = this;
          axios(
              {
                  method: 'get',
                  url: "http://"+login.ip+"/api/user/"+login.username+"/publishedTask",
                  params:{
                      status: 1,
                  },
                  headers: {
                      'username': login.username,
                      'token': login.token
                  }
              })
              .then(function (response) {
                  console.log(response);
                  that.tasks = response.data;
                  for(let t of  that.tasks) {
                      t.properties = JSON.parse(t.properties);
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
      changeTask(id){
          //初始化报名工人列表
          this.selectedKeys=[];
          this.selectedUsers=[];
          //自由分配则无视
          if(id==0){
              this.task = {
                              id:0,
                              skill:"java",
                              g:3,
                              alpha:0.8,
                          };
              this.userData = [];
              this.models = [];
              return;
          }
          let that = this;
          axios(
              {
                  method: 'get',
                  url: "http://"+login.ip+"/api/task/"+id+"/assignInfo",
                  params:{
                      status: 1,
                  },
                  headers: {
                      'username': login.username,
                      'token': login.token
                  }
              })
              .then(function (response) {
                  console.log(response.data);
                  let data = response.data;
                  let modules = [];
                  for(m of data.modules){
                      let temp={};
                      temp.id = m.id;
                      temp.complexity = 1;
                      modules.push(temp);
                  }
                  if(modules.length==0){
                      let temp={};
                      temp.id = that.currentTaskId;
                      temp.complexity = 1;
                      modules.push(temp);
                  }
                  that.models = modules;
                  let users = [];
                  let i = 0;
                  for(r of data.registers){
                      let temp={};
                      temp.id = i;
                      i++;
                      temp.username = r.username;
                      temp.nickname = r.name;
                      temp.skillMap = JSON.parse(r.skillList);
                      console.log(JSON.parse(r.extention).activeTime);
                      temp.workTime = that.getIntRangeByArray(JSON.parse(r.extention).activeTime);
                      users.push(temp);
                  }
                  that.userData = users;
                  for (let t of that.tasks) {
                      if(t.id == that.currentTaskId){
                          that.task.id = t.id;
                          that.task.skill = t.properties.skills;
                          break;
                      }
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
      getIntRangeByArray(array){
          let r = 0;
          for(let i of array){
              r=r+timeMap[i];
          }
          return r;
      },
      getSkillAbility(map,skill){
            for(let key in map){
                if(key.toLowerCase() == skill.toLowerCase()){
                    return map[key];
                }
            }
      },
      submitAssgin(){//提交分配结果到后台
            let that = this;
           //构建模块id——工人username map
           let assginMap={};
           for(let m of that.assignResult.models){
                let key = m.id;
                let userIndex = m.worker.id;
                let username = that.userData[userIndex].username;
                assginMap[key] = username;
           }
           console.log(assginMap);
                        axios(
                                      {
                                          method: 'post',
                                          url: "http://"+login.ip+"/api/task/"+that.task.id+"/assignSubmit",
                                          headers: {
                                              'username': login.username,
                                              'token': login.token
                                          },
                                          data: assginMap
                                      })
                          .then(function (response) {
                              let data= response.data;
                              console.log(data);
                              let msg = {};
                              if(data.success){
                                  msg.message = data.message;
                                  msg.type = 'success';
                              }else{
                                  msg.message = data.message;
                                  msg.type = 'error';
                              }
                              that.$message(msg);
                          })
                          .catch(function (error) {
                              console.log(error);
                              that.$message({
                                  message: '网络错误！',
                                  type: 'error'
                              });
                          });

      }
  },
    mounted: function () {
        this.getTasksList();
    }
})