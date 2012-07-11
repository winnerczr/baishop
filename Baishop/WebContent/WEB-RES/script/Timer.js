

function Timer(){
	this.timerID;
	this.handle;
	this.delay;
}

Timer.prototype.schedule=function(func,delay){
	this.handle = func;
	this.delay = delay;
	timerID = window.setInterval(
		this.handle,
	    this.delay
    );
};


Timer.prototype.cancel=function(){
	if(this.timerID!=null && this.timerID!='undefine')
		window.clearInterval(this.timerID);
};