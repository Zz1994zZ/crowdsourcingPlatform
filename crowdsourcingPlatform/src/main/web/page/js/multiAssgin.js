var timeMap =[];
for(let i=1;i<=24;i++){
    timeMap[i]= 1<<(24-i);
}
var app = new Vue({
  el: '#app',
  data: {
       tasks:[
              {
                  "id":1994,
                   "g":1,
                   "skill":"java",
                   "alpha":0.5,
                   "registers":[0,1,2,3,4,5,6,7,8,9],
                   "models":[
                      {"id":1021,"complexity":0.6},
                      {"id":1022,"complexity":0.7}
                   ]
              },
             {
                 "id":2019,
                  "g":2,
                  "skill":"java",
                  "alpha":0.5,
                  "registers":[0,1,2,3,4,5,6,7,8,9],
                  "models":[
                     {"id":1023,"complexity":0.4},
                     {"id":1024,"complexity":0.2}
                  ]
             }
           ],

      type:2,

      //添加模块弹框
      dialogFormVisible:false,
      //任务配置弹框
      taskDialogFormVisible: false,
      //添加工人弹框
      workerDialogFormVisible: false,
      //三个弹框绑定对象
      model:{},
      worker:  {"id":1,"nickname":"nickname","workTime":1,"skillMap":'{"java":0.0,"php":0.0}'},
      //当前操作的任务
      task:{ "id":0,
           "g":0,
           "skill":"空",
           "alpha":0
      },

      //被选中的已录入工人
      selectedKeys:[],
      //左边工人数据/录入表格数据
      userData: [
          {
              id:0,
              nickname:"d1",
              username:"username1",
              skillMap:{java:0.8},
              workTime:62400
          },
          {
              id:1,
              nickname:"d2",
              username:"username2",
              skillMap:{java:0.7},
              workTime:960
          },
          {
              id:2,
              nickname:"d3",
              username:"username3",
              skillMap:{java:0.6},
              workTime:61440
          },
          {
              id:3,
              nickname:"d4",
              username:"username4",
              skillMap:{java:0.5},
              workTime:496
          },
          {
              id:4,
              nickname:"d5",
              username:"username5",
              skillMap:{java:0.4},
              workTime:16515073
          },
          {
              id:5,
              nickname:"d6",
              username:"username6",
              skillMap:{java:0.36},
              workTime:14680064
          },
          {
              id:6,
              nickname:"d7",
              username:"username7",
              skillMap:{java:0.33},
              workTime:16777215
          },
          {
              id:7,
              nickname:"d8",
              username:"username8",
              skillMap:{java:0.25},
              workTime:60
          },
          {
              id:8,
              nickname:"d9",
              username:"username9",
              skillMap:{java:0.24},
              workTime:253952
          },
          {
              id:9,
              nickname:"d10",
              username:"username10",
              skillMap:{java:0.15},
              workTime:16128
          }
      ],
      //右边数据（分配时提交的）
      selectedUsers: [],
      assignResult:null
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
      addModel(task){
          this.task.models.push(this.model);
          this.model = {};
          this.dialogFormVisible = false;
      },
      //删除被选中的工人
      delWokers(){
          this.userData = this.userData.filter(item => { return this.selectedKeys.every(data => data!== item.id) });
      },
      //任务列表相关
      handleEdit(task,index, row) {
          console.log(index, row);
      },
      handleDelete(task,index, row) {
          console.log(index, row);
          // let i = 0;
          // for(;i<this.models.length;i++){
          //     if(this.models[i].id == row.id)
          //         break;
          // }
          task.models.splice(index, 1);
      }
      ,
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
                let tasks = [];
                for(let t of that.tasks){
                    if(t.models.length>0){
                        tasks.push(t);
                    }
                }
                let users = this.userData;
            axios(
                          {
                              method: 'post',
                              url: "http://"+login.ip+"/api/massign",
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
                                      message: '分配失败，请检查配置或剔除部分任务！',
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
      findUserById(id){
        for(let user of this.userData){
            if(user.id == id){
                return user;
            }
        }
      },
      confTask(task){
        this.task = task;
        this.taskDialogFormVisible = true;
      },
      confModels(task){
         this.task = task;
         this.dialogFormVisible = true;
      },
      addTask(){
           this.task = { "id":'',
                          "g":0,
                          "skill":"空",
                          "alpha":0,
                          "registers":[],
                          "models":[],
                          "isNew":true
                       };
           this.taskDialogFormVisible = true;
      },
      addTaskSubmit(){
            delete this.task.isNew;
            this.tasks.push(this.task);
            this.taskDialogFormVisible = false;
      },
      modulsArrayTran(modules){
            let result = [];
            for(let m of modules){
                m.properties = JSON.parse(m.properties)
                let temp = {};
                temp.id=m.id;
                temp.complexity= m.properties.price;
                result.push(temp);
            }
            return result;
      },
      registersArrayTran(registers,userData){
          let result = [];
          for(let r of registers){
              for(let u of userData){
                  if(u.username == r.username){
                      result.push(u.id);
                      break;
                  }
              }
          }
          return result;
      },
      getMutilTasksInfo(){
          let that = this;
          axios(
              {
                  method: 'GET',
                  url: "http://"+login.ip+"/api/task/mutilAssignInfo",
                  headers: {
                      'username': login.username,
                      'token': login.token
                  }
              })
              .then(function (response) {
                  if(response.data){
                      console.log(response.data);
                      let users = {};
                      let rTasks = [];
                      for(let data of response.data){

                          let task = data.task;
                          let modules = data.modules;
                          let registers = data.registers;

                          task.properties = JSON.parse(task.properties);
                          //将所有用户加入usersmap
                          for(let r of registers){
                              if(!users[r.username]){
                                  r.extention = JSON.parse(r.extention);
                                  users[r.username] = r;
                              }
                          }
                          let temp = {};
                          temp.id = task.id;
                          temp.g = 1;
                          temp.skill = task.properties.skills;
                          temp.alpha = 0.8;
                          temp.registers = registers;
                          if(task.properties.crowdNum==1){
                              temp.models = [{"id":task.id,"complexity":task.properties.price}];
                          }else{
                              temp.models = that.modulsArrayTran(modules);
                          }
                          rTasks.push(temp);
                      }
                      //usersmap转化为userData
                      let i =0;
                      let rUsers = [];
                      for(let username in users){
                          let userInfo = users[username];
                         let temp = {};
                          temp.id = i;
                          i++;
                          temp.nickname = userInfo.name;
                          temp.username = userInfo.username;
                          temp.skillMap = JSON.parse(userInfo.skillList);
                          // console.log(JSON.parse(userInfo.extention).activeTime);
                          temp.workTime = that.getIntRangeByArray(userInfo.extention.activeTime);
                          console.log(temp);
                          rUsers.push(temp);
                      }
                      that.userData = rUsers;
                      //tasks配置
                      console.log(rTasks);
                      for(let task of rTasks){
                          task.registers =that.registersArrayTran(task.registers,rUsers);
                      }
                      // that.tramsAvgComplex(rTasks);
                      that.tasks = rTasks;
                      that.$message({
                          message: '获取数据成功！',
                          type: 'success'
                      });
                  }else{
                      that.$message({
                          message: '暂时没有可分配任务或者您的权限不够哦~！',
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
      caculateECofM(x,avg){
          return (Math.atan(x-avg)+Math.atan(avg))/(Math.PI/2+Math.atan(avg));
      },
      tramsAvgComplex(tasks){
          let num =0;
          let sum = 0;
          let max = 0;
          let priceArray = [];
          for(let task of tasks){
              for(let m of task.models){
                  sum+=m.complexity;
                  priceArray.push(m.complexity);
                  if(m.complexity>max){
                      max=m.complexity;
                  }
                  num++;
              }
          }
          let avg = sum/num;
          let middle = priceArray.sort()[parseInt(num/2)];
          if(num>0){
              for(let task of tasks){
                  for(let m of task.models){
                    m.complexity = m.complexity/max;
                    //   m.complexity = this.caculateECofM(m.complexity,avg);
                    //   m.complexity = this.caculateECofM(m.complexity,middle);
                      m.complexity  = m.complexity .toFixed(2)
                  }
              }
          }
      },
      getIntRangeByArray(array){
          let r = 0;
          for(let i of array){
              r=r+timeMap[i];
          }
          return r;
      },
      submitAssgin(){//提交分配结果到后台
          let that = this;
          //构建模块id——工人username map
          let assginTaskMap={};
          for(let assignResultTask of that.assignResult){
              let assginMap={};
              for(let m of assignResultTask.models){
                  let key = m.id;
                  let userIndex = m.worker.id;
                  let username = that.userData[userIndex].username;
                  assginMap[key] = username;
              }
              assginTaskMap[assignResultTask.id]=assginMap;
          }
          console.log(assginTaskMap);
          axios(
              {
                  method: 'post',
                  url: "http://"+login.ip+"/api/task/mutilAssignSubmit",
                  headers: {
                      'username': login.username,
                      'token': login.token
                  },
                  data: assginTaskMap
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

      },
      getSkillAbility(map,skill){
          for(let key in map){
              if(key.toLowerCase() == skill.toLowerCase()){
                  return map[key];
              }
          }
      }
  },
    mounted: function () {
        this.getMutilTasksInfo();
    }
})