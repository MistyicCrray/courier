(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["pages/user/forgot/forgot"],{"0a65":function(e,t,n){"use strict";var o=function(){var e=this,t=e.$createElement;e._self._c;e._isMounted||(e.e0=function(t){!e.safety.state&&e.fnGetPhoneCode()})},s=[];n.d(t,"a",function(){return o}),n.d(t,"b",function(){return s})},"3cdb":function(e,t,n){},"78d0":function(e,t,n){"use strict";n.r(t);var o=n("0a65"),s=n("b736");for(var a in s)"default"!==a&&function(e){n.d(t,e,function(){return s[e]})}(a);n("ff9e");var i=n("2877"),r=Object(i["a"])(s["default"],o["a"],o["b"],!1,null,null,null);t["default"]=r.exports},b736:function(e,t,n){"use strict";n.r(t);var o=n("bc6f"),s=n.n(o);for(var a in o)"default"!==a&&function(e){n.d(t,e,function(){return o[e]})}(a);t["default"]=s.a},bc6f:function(e,t,n){"use strict";(function(e,o){Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var s=n("7e1a"),a=function(){return n.e("components/cmd-nav-bar/cmd-nav-bar").then(n.bind(null,"e2f3"))},i=function(){return n.e("components/cmd-page-body/cmd-page-body").then(n.bind(null,"76fb"))},r=function(){return n.e("components/cmd-transition/cmd-transition").then(n.bind(null,"4129"))},u=function(){return n.e("components/cmd-input/cmd-input").then(n.bind(null,"591c"))},c={components:{cmdNavBar:a,cmdPageBody:i,cmdTransition:r,cmdInput:u},data:function(){return{user:{email:"",code:"",passwordOne:"",passwordTwo:""},passwordReg:/^\w+$/,phoneReg:/^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/,safety:{time:60,state:!1,interval:""},forgotMobile:!1}},methods:{fnForgot:function(){var t=this;if(""!=t.user.code)if(""!=t.user.passwordOne)if(""!=t.user.passwordTwo){if(t.user.passwordTwo==t.user.passwordOne){var n="user/emailUpdatePwd";return(0,s.getRquest)(n,t.user).then(function(t){console.info(o(t," at pages\\user\\forgot\\forgot.vue:97")),200==t.code&&(e.showToast({title:"修改成功",icon:"none"}),e.navigateTo({url:"../login/login"}))})}e.showToast({title:"密码与确认密码不一致",icon:"none"})}else e.showToast({title:"请输入确认密码",icon:"none"});else e.showToast({title:"请输入密码",icon:"none"});else e.showToast({title:"请输入验证码",icon:"none"})},fnGetPhoneCode:function(){var t=this;if(this.phoneReg.test(this.user.email)){var n="user/sendEmail/"+this.user.email;return(0,s.getRquest)(n,"").then(function(n){e.showToast({title:"正在发送验证码",icon:"loading",success:function(){t.safety.state=!0,t.safety.interval=setInterval(function(){t.safety.time--<=0&&(t.safety.time=60,t.safety.state=!1,clearInterval(t.safety.interval))},1e3),e.showToast({title:"发送成功",icon:"success"})}})}).catch(function(e){})}e.showToast({title:"邮箱不正确",icon:"none"})}},beforeDestroy:function(){clearInterval(this.safety.interval)}};t.default=c}).call(this,n("6e42")["default"],n("0de9")["default"])},ff9e:function(e,t,n){"use strict";var o=n("3cdb"),s=n.n(o);s.a}},[["0b9e","common/runtime","common/vendor"]]]);