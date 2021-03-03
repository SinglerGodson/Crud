/***
 * 无限极联动选择组件封装模块
 */
layui.define(['jquery', 'form', 'admin', 'config', 'selectData'], function (exports) {
    var $ = layui.$, form = layui.form, admin = layui.admin, config = layui.config,
        selectData = layui.selectData;

    var BASE_URL = config.sourcing_server + "/platform/form/selectable/";
    var selects = $("select[service-name]"); // 所有带有service-mame属性的选择框

    if (selects && selects.length > 0) {
        var selectMap = toSelectMap(selects);
        $.each(selectMap, function (serviceName, selects) {
            init(selects, serviceName);
        });
    }

    function toSelectMap(selects) {
        var selectMap = {};
        $.each(selects, function (index, select) {
            var serviceName = $(select).attr("service-name");
            var selects = selectMap[serviceName];
            if (!selects) {
                selects = [];
            }
            selects.push(select);
            selectMap[serviceName] = selects;
        });
        return selectMap;
    }

    function init(selects, serviceName, params, data, selectedValue, valueName, labelName) {
        if (data) {
            render(selects, data, selectedValue, valueName, labelName);
        } else if (serviceName) {
            // ajax请求
            admin.req(BASE_URL + serviceName, params, function (respData) {
                if (respData.code === '0') {
                    data = respData.data;
                } else {
                    data = selectData[serviceName];
                }
                render(selects, data, selectedValue, valueName, labelName);
            }, "GET");
        }
    }

    function render(selects, data, selectedValue, valueName, labelName) {
        var options = "";
        if (!valueName) valueName = "value";
        if (!labelName) labelName = "label";
        $.each(data, function (i, d) {
            options += "<option value='" + d[valueName] + "'>" + d[labelName] + "</option>";
        });
        $.each(selects, function (i, s) {
            $(s).append(options);
            var selected = $(s).attr("data-value");
            if (!selected && selectedValue) {
                selected = selectedValue;
            }
            if (selected) {
                $(s).val(selected);
            }
        });
        form.render("select");
    }

    var SimpleSelect = {
        init: init
    };

    exports('simpleSelect', SimpleSelect);
});