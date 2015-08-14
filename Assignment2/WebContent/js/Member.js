function Member(member, isMember) {
    if (isMember) {
        for (var p in member) {
            if (!member.hasOwnProperty(p)) continue;
            this[p] = member[p];
        }
    }
    else {
        this.id = member['id'];
        this.name = (member['name'] === 'null') ? '' : member['name'];
        this.password = (member['password'] === 'null') ? '' : member['password'];
        this.gender = (member['gender'] === 'null') ? '' : member['gender'];
        if (member['birthday'] != 'null') {
            this.birthday = new Date(parseInt(member['birthday'], 10)).Format('yyyy/MM/dd');
        } else {
            this.birthday = '';
        }
        this.age = (member['age'] === 'null') ? '' : member['age'];
        this.contact = (member['contact'] === 'null') ? '' : member['contact'];
        this.address = (member['address'] === 'null') ? '' : member['address'];
        if (member['joinDate'] != 'null') {
            this.joindate = new Date(parseInt(member['joinDate'], 10)).Format('yyyy/MM/dd');
        } else {
            this.joindate = '';
        }
        this.joindays = (member['joinDays'] === 'null') ? '' : member['joinDays'];
        this.addition = (member['addition'] === 'null') ? '' : member['addition'];
        this.writable = parseInt(member['writable'], 10);
    }
}

Member.prototype.getId = function () {
    return this.id;
};

Member.prototype.getName = function () {
    return this.name;
};

Member.prototype.getPassword = function () {
    return this.password;
};

Member.prototype.getGender = function () {
    return this.gender;
};

Member.prototype.getBirthday = function () {
    return this.birthday;
};

Member.prototype.getAge = function () {
    return this.age;
};

Member.prototype.getContact = function () {
    return this.contact;
};

Member.prototype.getAddress = function () {
    return this.address;
};

Member.prototype.getJoinDate = function () {
    return this.joindate;
};

Member.prototype.getJoinDays = function () {
    return this.joindays;
};

Member.prototype.getAddition = function () {
    return this.addition;
};

Member.prototype.getWritable = function () {
    return this.writable;
};

Member.prototype.toString = function () {
    var result = '';
    for (var p in this) {
        if (!this.hasOwnProperty(p)) continue;
        result += '[' + p + '=' + this[p] + ']';
    }
    return result;
};

// 瀵笵ate鐨勬墿灞曪紝灏�Date 杞寲涓烘寚瀹氭牸寮忕殑String
// 鏈�M)銆佹棩(d)銆佸皬鏃�h)銆佸垎(m)銆佺(s)銆佸搴�q) 鍙互鐢�1-2 涓崰浣嶇锛�// 骞�y)鍙互鐢�1-4 涓崰浣嶇锛屾绉�S)鍙兘鐢�1 涓崰浣嶇(鏄�1-3 浣嶇殑鏁板瓧)
// 渚嬪瓙锛�// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1,                 //鏈堜唤
        "d+": this.getDate(),                    //鏃�        "h+": this.getHours(),                   //灏忔椂
        "m+": this.getMinutes(),                 //鍒�        "s+": this.getSeconds(),                 //绉�        "q+": Math.floor((this.getMonth() + 3) / 3), //瀛ｅ害
        "S": this.getMilliseconds()             //姣
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}