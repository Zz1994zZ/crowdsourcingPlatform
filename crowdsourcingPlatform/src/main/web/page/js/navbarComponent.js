Vue.component('xinhuo-navbar', {
    props: ['activeIndex'],
    template:`<div>
    <div  class="navbar-wrapper">
      <div class="container">
        <nav class="navbar navbar-inverse navbar-static-top">
          <div class="container">
            <div class="navbar-header">
              <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
              </button>
              <a class="navbar-brand" href="#"></a>
            </div>
            <div  class="navbar-collapse collapse">
              <ul class="nav navbar-nav">
                <li v-bind:class="activeIndex == 1 ? 'active':''"><a href="index.html">首页</a></li>
                <li v-bind:class="activeIndex == 2 ? 'active':''"><a href="tasks.html">接任务</a></li>
                <li v-bind:class="activeIndex == 3 ? 'active':''"><a href="taskPost.html">找帮手</a></li>
              </ul>
              <ul class="nav navbar-nav navbar-right">
                <!--<li v-if="token!=''"><a href="#about" @click="">{{username}}</a></li>-->
                <li v-if="token!=''">
                  <a>
                  <el-dropdown trigger="click"  @command="handleCommand">
                      <span class="el-dropdown-link">
                        {{username}}<i class="el-icon-caret-bottom el-icon--right"></i>
                      </span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item class="clearfix" command="workPlatform">
                        工作台
                        <el-badge class="mark" :value="12" />
                      </el-dropdown-item>
                      <el-dropdown-item class="clearfix" command="message">
                        消息
                        <el-badge class="mark" :value="3" />
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                  <a>
                </li>
                <li v-if="token==''"><a href="#about" @click="dialogFormVisible = true">登陆</a></li>
                <li v-if="token!=''"><a href="#contact" @click="onLogout()">注销</a></li>
              </ul>
            </div>
          </div>
        </nav>
      </div>
    </div>
    <el-dialog :visible.sync="dialogFormVisible" width="400px" >
      <div class="form-signin" style="z-index: 2002">
        <h2 class="form-signin-heading">Please sign in</h2>
        <label for="inputEmail" class="sr-only">Email address</label>
        <input v-model="username"  id="inputEmail" class="form-control" placeholder="Username" required autofocus>
        <label for="inputPassword" class="sr-only">Password</label>
        <input v-model="password" type="password" id="inputPassword" class="form-control" placeholder="Password" required>
        <div class="checkbox">
          <label>
            <input type="checkbox" value="remember-me"> Remember me
          </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" v-on:click="onSubmit()">Sign in</button>
      </div>
    </el-dialog>
  </div>`,
        data: function () {
            return {
                ip: 'localhost:8080',
                dialogFormVisible: false,
                username: '',
                password: '',
                loginLabel: '登陆',

                //登陆授权token
                token: '',
                loginData: ''
            }
        },
        methods: {
            handleCommand(command) {
                window.location.href=command+".html";
            },
            onSubmit() {
                let that = this;
                if (!$.trim(this.username) || !$.trim(this.password)) {
                    return;
                }
                axios.post('http://localhost:8080/api/token', {
                    username: this.username,
                    password: this.password
                })
                    .then(function (response) {
                        console.log(response);
                        let data = response.data;
                        let msg = data.message;
                        let type = data.success ? 'success' : 'error';
                        that.loginData = data.data;
                        if (that.loginData) {
                            that.token = that.loginData.tokenCode;
                            sessionStorage.setItem('token', that.token);
                            sessionStorage.setItem('username', that.username);
                            that.dialogFormVisible = false;
                            that.onFinishLogin();
                        }
                        that.$message({
                            message: msg,
                            type: type
                        });
                    })
                    .catch(function (error) {
                        console.log(error);
                        that.$message({
                            message: '网络错误！',
                            type: 'error'
                        });
                    });
            },
            onLogout() {
                this.token = '';
                this.username = '';
                sessionStorage['token'] = '';
                sessionStorage['username'] = '';
            },
            onFinishLogin() {
                this.$emit('finishlogin', this.ip, this.token, this.username)
            }
        },
        mounted: function () {
            let that = this;
            let username = sessionStorage['username'];
            let token = sessionStorage['token'];
            if (username && token) {
                that.username = username;
                that.token = token;
                that.onFinishLogin();
            } else {
                that.dialogFormVisible = true;
            }
        }
})

var login = new Vue({
    el: '#login',
    data:{
        ip: 'localhost:8080',
        username: '',
        //登陆授权token
        token: '',
        loginData: ''
    },
    methods: {
        onGotToken() {
            this.ip = arguments[0];
            this.token = arguments[1];
            this.username = arguments[2];
            this.onFinishLogin();
        },
        onFinishLogin(){

        }
    }
})