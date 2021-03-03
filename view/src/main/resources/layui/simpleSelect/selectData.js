layui.define(['constants'], function (exports) {
    var constants = layui.constants;

    function transfer(data, valueName, labelName) {
        var selectData = [];
        if (!valueName) valueName = 'code';
        if (!labelName) labelName = 'desc';
        $.each(data, function(index, d) {
            selectData.push({value: d[valueName], label: d[labelName]})
        });
        return selectData;
    }

    var selectData = {
        enquiryResult: transfer(constants.enquiry.result),
        enquiryExecMode: transfer(constants.enquiry.execMode),
    };

    exports('selectData', selectData);
});