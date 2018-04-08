# single-sign-on
学习用
使用filter+cookie实现单点登陆，在用户持有有效cookie第一次使用浏览器访问index.jsp时，将自动转发到登陆成功页面

<br>①浏览器第一次发送请求到index.jsp，请求被MyFilter01拦截，并查看用户是否已登陆，使用session对象的state属性来验证，如果state为null，则认为不是处于登陆状态
<br>②查看用户是否带有cookie，如果有，对cookie进行三步验证：		<br>1.cookies中是否有关于自动登陆的cookie		<br>2.cookie长度是否合法		<br>3.查看cookie中的用户信息是否和数据库中信息符合，以上三点有一点不符合则放行登陆界面index.jsp，让用户手动登陆
<br>③用户登陆成功后根据用户信息构建cookie，并设置寿命，连同登陆成功页面一同发送给用户，同时设置session对象的state属性值为ok
<br>④MyFilter01拦截response，进行相关处理
<br>⑤用户再次访问index.jsp时，MyFilter01拦截请求，发现session对象的state属性值为ok而不是null，于是直接跳转到登陆成功页面，显示已登陆状态
<br>⑥用户关闭浏览器，session对象被回收，用户再次打开浏览器访问index.jsp，MyFilter01劫持请求，发现session对象的state属性为null，出发cookie验证，取得用户cookie，再次进行三步验证，不成功者放行index.jsp页面要求用户登陆，成功者直接转发到登陆成功页面，并显示是cookie登陆，同时设置session对象state属性值为ok