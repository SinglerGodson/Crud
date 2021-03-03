/***
 * 无限极联动选择组件封装模块
 */
layui.define(['jquery', 'form'], function () {

    $("input.num-only").keyup(numberOnly);
    $("input").not("[allow-special-char]").keyup(noneSpecialChar);

    // 只能输入数字或者带有两位小数的数字
    function numberOnly() {
        var value = this.value;
        value = value.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符
        value = value.replace(/^\./g,""); //验证第一个字符是数字
        value = value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
        value = value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
        value = value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');//只能输入两个小数
        if (value.indexOf(".") < 0 && value !== "") {//以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
            this.value = parseFloat(value);
        }
    }

    /**
     * 特殊符号
     */
    function noneSpecialChar() {

        var rs = "";
        var value = this.value;
        var allowSpecialChar = $(this).attr("allow-special-char");
        var allSpecialChar = "[`~!@#$%^&*()+=|{}':;,\\[\\]\\\\.<>/?~！@#￥……&*（）——|{}【】‘’；：“”。，、？]";
        if (allowSpecialChar) {
            var specialChar = "";
            var allowPatter = new RegExp("[" + allowSpecialChar + "]");
            for (var i = 0; i < allSpecialChar.length; i++) {
                specialChar = specialChar + allSpecialChar.substr(i, 1).replace(allowPatter, '');
            }
            allSpecialChar = specialChar;
        }
        var pattern = new RegExp(allSpecialChar);
        for (var i = 0; i < value.length; i++) {
            rs = rs + value.substr(i, 1).replace(pattern, '');
        }
        if (value.length > rs.length) {
            console.log("若要输入特殊字符，请添加allow-special-char=''属性");
        }
        this.value = rs;
    }

});