github token:  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

2.git init //把这个目录变成git可以管理的仓库，并且是一个空仓库
3.git config --global user.email"邮箱" //绑定github邮箱和名字
git config --global user.name"名字"
4.git add . //把当前文件夹下面的文件都添加进来，注意后面的 . 不能忘
git commit -m “first commit” //-m后面输入的是本次提交的说明，可以输入任意内容，最好是有意义的。
5.git remote add origin https://github.com//.git
//origin后面是自己的仓库url地址
6.git remote set-url origin https://<你的token>@github.com/<GitHub用户名>/<要提交到的库名>.git
//7.git config http.sslVerify “false” //更改网络认证设置
8.git push -u origin main/master //将已绑定的本地仓库push上去