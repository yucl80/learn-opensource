let sleepFun = (time) => new Promise((resolve) => setTimeout(resolve, time));

async function a() {
   console.log("aa");
   for( i = 0; i < 1000000; i++){
          eval("1+231231+13");

   }
   console.log("aa");
}
async function b() {
  console.log("bb");

}

async function f() {
    let a1 = a();
    console.log("vvv");
    let b1 = b();
    console.log("xxx");
    await a1;
    await b1;

  return await Promise.resolve('hello world');
}

f()
.then(v => console.log(v))
.catch(e => console.log(e))