Android app that acts as a shim for data uri handling when trying to launch the browser via an intent.

This app is a proof of concept only, I don't expect anyone to actually use it in production.

It would be nice if browsers registered for data URIs, then you could launch content without needing to be online.

If you dump this Data URI into your address bar, the page will render correctly.

```
data:text/html,data:text/html,<html><body><canvas id="c"></canvas><script>var a=document.getElementById('c');var b=document.body;var d=function(e){return function(){e.parentNode.removeChild(e);};}(a);var requestAnimationFrame = window.requestAnimationFrame || window.mozRequestAnimationFrame || window.webkitRequestAnimationFrame || window.msRequestAnimationFrame || function(f){ setTimeout(f, 1000/30); };var c = a.getContext('2d');function M(s,i,n){return s+(i-s)*n}function S(s){return s*s*(3-2*s)}function $(i,a,r){i=A(i+s)*r,a=A(a)*r;var t=S(i-~~i),e=S(a-~~a),o=s-1,u=~~i&o,c=~~a&o,d=u+1&o,k=c+1&o,f=M(n[c*o+u],n[c*o+d],t),m=M(n[k*o+u],n[k*o+d],t);return M(f,m,e)}function G(){for(T+=.02,i=J;s*s-J>i;i++){var n,a,t,e,o=i%s/s,u=1-~~(i/s)/s,M=2*o-1-Q(T)/8,f=2*u-1.5,m=-1.15,h=20*Q(T),A=140,b=160+30*Q(T),g=0,v=0,q=0,D=0,I=50;for(k=0,E=1;I>k&&(D=A,t=h*h+A*A+b*b,e=$(h,b,.05)+.2*$(h,b,.5),t>s*s?(D=k=0,g=v=q=A/1e3):(D=A-90*e,g=v=q=4.6*k/I,g*=.3,q/=5,v*=.1,n=.7*A-55,50>b&&A+40*D>74&&(g=S(2*g),q=.95*S(1.75*q),v=S(1*v)),(a=R(h*h+n*n+b*b)-32)<D&&(D=a,g=q=v=1-k/I*1.2*(1-n/4)*1-.3*$(h,A,.9)-.03*Q(i*e/500),q*=.8,g*=.9,v*=.7)),!(.2>D));k++)h+=M*D,A+=f*D,b+=m*D;g+=.1,v+=.05,q+=.08,d[4*i]=g*s,d[4*i+1]=q*s,d[4*i+2]=v*s,d[4*i+3]=s}c.putImageData(r,0,-40),requestAnimationFrame(G)}var s=256,i=0,k=0,J=40*s,r=c.createImageData(s,s),d=r.data,n=[],T=0,Q=Math.sin,R=Math.sqrt,A=Math.abs;for(b.style.background="#000",i=0;s*s>i;i++)n[i]=M(.7,.5,Math.random());G();</script></body></html>`
```

However, if you scan the below QR code before you have installed the app, the device will probably complain, even though it's the exact same content. After installing the app, it will launch a website, even if you aren't connected to the Internet. As far as I can tell, the only thing Google needs to do to support this is make Chromium accept Data URIs in the Manifest file.

![QR Code](https://raw.githubusercontent.com/madmaw/android-data-uri-service/master/qrcode.png)

You can download the latest build of the app from https://raw.githubusercontent.com/madmaw/android-data-uri-service/master/target/adus-1.0.apk

I didn't make the demo, but took it from here http://js1k.com/2014-dragons/demo/1664

I've logged a bug with Google to add data URI handling to Chrome for Android https://code.google.com/p/chromium/issues/detail?id=399814