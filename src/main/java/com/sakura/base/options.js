export const sortOptions = [
  {
    label: '请选择排序类型',
    value: 0,
  },
  {
    label: '交换排序（两者进行位置互换，只改变两者的序号，其它不变）',
    value: 1,
  },
  {
    label: '冒泡排序（两者进行大小比较，较小的会排在前面，正序排列）',
    value: 2,
  },
]
export const stepOptions = [
  {
    label: '打开网页',
    value: '0',
    tip: '使用指定的浏览器打开网页',
  },
  {
    label: '关闭网页',
    value: '1',
    tip: '关闭当前网页',
  },
  {
    label: '切换窗口',
    value: '2',
    tip: '切换到指定窗口',
  },
  {
    label: '设置窗口大小',
    value: '3',
    tip: '设置窗口大小',
  },
  {
    label: '选择内嵌网页',
    value: '4',
    tip: '从当前 window 选择 frame(对 frame 标签起作用)',
  },
]
export const switchOptions = [
  {
    label: '根据句柄ID切换到指定窗口',
    value: '0',
  },
  {
    label: '根据网页索引号切换到指定窗口',
    value: '1',
  },
  {
    label: '切换到初始窗口',
    value: '2',
  },
]
export const sizeOptions = [
  {
    label: '全屏',
    value: '0',
  },
  {
    label: '指定尺寸（像素为单位）',
    value: '1',
  },
]
export const innerWebOptions = [
  {
    label: '退出当前frame（回到主页面）',
    value: '0',
  },
  {
    label: '根据frame索引号切换到指定frame',
    value: '1',
  },
  {
    label: '根据定位方式切换frame',
    value: '2',
  },
]
export const objectOptions = [
  {
    label: '元素对象',
    value: '0',
  },
  {
    label: '元素定位',
    value: '1',
  },
]
export const locateTypeOptions = [
  {
    label: 'id',
    id: '0',
    value: '0',
  },
  {
    label: 'name',
    id: '1',
    value: '1',
  },
  {
    label: 'class',
    id: '2',
    value: '2',
  },
  {
    label: 'tag',
    id: '3',
    value: '3',
  },
  {
    label: 'linkText',
    id: '4',
    value: '4',
  },
  {
    label: 'plink',
    id: '5',
    value: '5',
  },
  {
    label: 'css',
    id: '6',
    value: '6',
  },
  {
    label: 'xpath',
    id: '7',
    value: '7',
  },
  {
    label: 'label',
    id: '8',
    value: '8',
  },
  {
    label: 'value',
    id: '9',
    value: '9',
  },
  {
    label: 'index',
    id: '10',
    value: '10',
  },
]
export const windowStepOptions = [
  {
    label: '弹窗操作',
    value: '0',
  },
]
export const inputOptions = [
  {
    label: '否',
    value: '0',
  },
  {
    label: '是',
    value: '1',
  },
]
export const functionOptions = [
  {
    label: '确定',
    value: '0',
  },
  {
    label: '取消',
    value: '1',
  },
]
export const inputStepOptions = [
  {
    label: '输入操作',
    value: '0',
  },
]
export const inputOperations = [
  {
    label: '输入框',
    value: '0',
  },
  {
    label: '可编辑段落',
    value: '1',
  },
]
export const mouseOperations = [
  {
    label: '鼠标点击',
    value: '0',
  },
  {
    label: '鼠标移动',
    value: '1',
  },
  {
    label: '鼠标拖拽',
    value: '2',
  },
]
export const clickOptions = [
  {
    label: '单击',
    value: '0',
  },
  {
    label: '双击',
    value: '1',
  },
  {
    label: '按下',
    value: '2',
  },
  {
    label: '弹起',
    value: '3',
  },
]
export const moveOptions = [
  {
    label: '鼠标移出元素',
    value: '0',
  },
  {
    label: '鼠标移入元素',
    value: '1',
  },
  {
    label: '鼠标从元素移到坐标位置',
    value: '2',
  },
]
export const elementOperations = [
  {
    label: '提交表单',
    value: '0',
  },
  {
    label: '下拉框操作',
    value: '1',
  },
  {
    label: '设置选项',
    value: '2',
  },
  {
    label: '等待元素',
    value: '3',
  },
]
export const eleOperations = [
  {
    label: '选择',
    value: '0',
  },
  {
    label: '取消',
    value: '1',
  },
]
export const waitOptions = [
  {
    label: '等待元素等于给定的定值（Text）',
    value: '0',
  },
  {
    label: '等待元素存在',
    value: '1',
  },
  {
    label: '等待元素显示',
    value: '2',
  },
  {
    label: '等待元素不显示',
    value: '3',
  },
  {
    label: '等待元素不存在',
    value: '4',
  },
  {
    label: '等待元素可编辑',
    value: '5',
  },
  {
    label: '等待元素不可编辑',
    value: '6',
  },
]
export const childSelectOperations = [
  {
    label: '选项（Option）',
    value: '0',
  },
  {
    label: '索引（Index）',
    value: '1',
  },
  {
    label: '值（value）',
    value: '2',
  },
]
export const loopStepOptions = [
  {
    label: '次数循环',
    value: '0',
  },
  {
    label: 'ForEach循环',
    value: '1',
  },
  {
    label: 'while循环',
    value: '2',
  },
  {
    label: 'if',
    value: '3',
  },
  {
    label: 'else if',
    value: '4',
  },
  {
    label: 'else',
    value: '5',
  },
]
export const conditionOptions = [
  {
    label: '条件列表',
    value: '0',
  },
  {
    label: '条件表达式',
    value: '1',
  },
]
export const conditions = [
  {
    label: '值_等于[value=]',
    value: '0',
  },
  {
    label: '值_不等于[value!=]',
    value: '1',
  },
  {
    label: '值_包含[include=]',
    value: '2',
  },
  {
    label: '长度_等于[length=]',
    value: '3',
  },
  {
    label: '长度_不等于[length!=]',
    value: '4',
  },
  {
    label: '长度_大于[length>]',
    value: '5',
  },
  {
    label: '长度_小于[length<]',
    value: '6',
  },
  {
    label: '正则匹配',
    value: '7',
  },
]
export const wayOptions = [
  {
    label: '终止流程',
    value: '0',
  },
  {
    label: '忽略异常并继续执行',
    value: '1',
  },
]
export const prepositionOptions = [
  {
    label: '前置脚本',
    value: '0',
  },
  {
    label: '等待时间',
    value: '1',
  },
  {
    label: '数据提取',
    value: '2',
  },
]
export const scriptTypeOptions = [
  {
    label: '同步',
    value: '0',
  },
  {
    label: '异步',
    value: '1',
  },
]
export const variantOptions = [
  {
    label: '无返回值',
    value: '0',
  },
  {
    label: '有返回值',
    value: '1',
  },
]
export const dataOptions = [
  {
    label: '提取窗口信息',
    value: '0',
  },
  {
    label: '提取元素信息',
    value: '1',
  },
]
export const windowOptions = [
  {
    label: '窗口Handle（storeWindowHandle）',
    value: '0',
  },
  {
    label: '网页标题（storeTitle）',
    value: '1',
  },
]
export const elementInfoOptions = [
  {
    label: '普通对象（store）',
    value: '0',
  },
  {
    label: '元素文本（store Text）',
    value: '1',
  },
  {
    label: '元素值（store Value）',
    value: '2',
  },
  {
    label: '元素属性（store Attribute）',
    value: '3',
  },
  {
    label: '匹配Xpath的元素数量（storeXpathCount）',
    value: '4',
  },
]
export const postPositionOptions = [
  {
    label: '后置脚本',
    value: '0',
  },
  {
    label: '等待时间',
    value: '1',
  },
  {
    label: '断言',
    value: '2',
  },
  {
    label: '数据提取',
    value: '3',
  },
]
export const assertOptions = [
  {
    label: '断言值',
    value: '0',
  },
  {
    label: '弹窗文本',
    value: '1',
  },
  {
    label: '元素断言',
    value: '2',
  },
  {
    label: '下拉框',
    value: '3',
  },
  {
    label: '网页标题',
    value: '4',
  },
]
export const expectsSelectOptions = [
  {
    label: '所选元素的值等于期望（SelectedValue）',
    value: '0',
  },
  {
    label: '下拉框选项显示的文本等于期望（SelectedLabel）',
    value: '1',
  },
  {
    label: '所选元素的值不等于期望（NotSelectedValue）',
    value: '2',
  },
]
export const expectElementOptions = [
  {
    label: '元素被选中（checked）',
    value: '0',
  },
  {
    label: '元素可编辑（Editable）',
    value: '1',
  },
  {
    label: '元素存在（ElementPresent）',
    value: '2',
  },
  {
    label: '元素不存在（ElementNotPresent）',
    value: '3',
  },
  {
    label: '元素未被选中（NotChecked）',
    value: '4',
  },
  {
    label: '元素不可编辑（NotEditable）',
    value: '5',
  },
  {
    label: '元素文本不等于期望（NotText）',
    value: '6',
  },
  {
    label: '元素文本等于期望（Text）',
    value: '7',
  },
  {
    label: '元素值等于期望（Value）',
    value: '8',
  },
]
export const levelOptions = [
  {
    label: 'P0',
    id: 'P0',
  },
  {
    label: 'P1',
    id: 'P1',
  },
  {
    label: 'P2',
    id: 'P2',
  },
  {
    label: 'P3',
    id: 'P3',
  },
]
export const executionModeOptions = [
  // {
  //   id: "0",
  //   name: '开发自测 '
  // },
  // {
  //   id: "1",
  //   name: '版本转测',
  // },
  {
    id: "2",
    name: '本地调试'
  },
  {
    id: "3",
    name: '远程调试',
  },
]
export const statusOptions = [
  {
    label: '未开始',
    id: '未开始',
  },
  {
    label: '进行中',
    id: '进行中',
  },
  {
    label: '已完成',
    id: '已完成',
  },
]
export const executionOptions = [
  {
    id: "0",
    name: '未开始',
  },
  {
    id: "1",
    name: '进行中',
  },
  {
    id: "2",
    name: '已完成',
  },
]
export const resultOptions = [
  {
    label: '不通过',
    id: '不通过',
  },
  {
    label: '全部通过',
    id: '全部通过',
  },
  {
    label: '-',
    id: '-',
  }
]
export const operations = [
  {
    name: '浏览器操作',
    id: '1',
    children: [
      {
        id: 'web-geturl',
        name: '打开默认网页',
        config: [{ paramsName: 'value', paramsValue: 'https://172.19.5.33/login' }],
      },
      {
        id: 'web-geturls',
        name: '打开指定网页',
        config: [{ paramsName: 'value', paramsValue: 'https://172.19.5.33/login' }],
      },
      {
        id: 'web-close',
        name: '关闭当前标签页',
        config:null
      },
      {
        id: 'web-quit',
        name: '关闭全部标签页',
        config:null
      },
      {
        id: 'web-refresh',
        name: '页面刷新',
        config:null
      },
      {
        id: 'web-getcode',
        name: '获取图片验证码',
        config: [
          { paramsName: 'locator', paramsValue: `xpath=(//input[@placeholder='验证码'])[1]`},
          { paramsName: 'url', paramsValue: `(//img[@title='点击刷新'])[1]`},
          { paramsName: 'element', paramsValue: `(//button[contains(text(),'登录')])[1]`},
          { paramsName: 'value', paramsValue: `(//div[@class='body-content'])[1]`},
          { paramsName: 'expect', paramsValue: `验证码错误`},
          { paramsName: 'message', paramsValue: `(//button[@type='button'][contains(text(),'确定')])[1]`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
      {
        id: 'switch-window',
        name: '切换当前最新窗口',
        config:null
      },
      {
        id: 'switch-windows',
        name: '切换指定窗口',
        config: [{ paramsName: 'value', paramsValue: '1' }],
      },
      {
        id: 'switch-Iframe',
        name: '切换Iframe控件',
        config: [
          { paramsName: 'value', paramsValue: `1`},
          { paramsName: 'locator', paramsValue: `xpath=(//iframe[@region='center'])[1]`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
      {
        id: 'return-Iframe',
        name: '返回上一级Iframe控件',
        config:null
      },
      {
        id: 'quit-Iframe',
        name: '返回最上级Iframe控件',
        config:null
      },
    ],
  },
  {
    name: '点击操作',
    id: '2',
    children: [
      {
        id: 'web-click',
        name: '元素点击',
        config: [
          { paramsName: 'locator', paramsValue: `xpath=(//button[@type='button'][contains(text(),'确定')])[1]`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
      {
        id: 'select-click',
        name: '选项框点击',
        config: [
          { paramsName: 'locator', paramsValue: `xpath=(//input[@placeholder='请选择'])[2]`},
          { paramsName: 'value', paramsValue: `(//span[contains(text(),'一般告警')])[1]`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
      {
        id: 'input-click',
        name: '选项框搜索点击',
        config: [
          { paramsName: 'locator', paramsValue: `xpath=(//input[@placeholder='请选择'])[1]`},
          { paramsName: 'value', paramsValue: `姓名`},
          { paramsName: 'element', paramsValue: `(//span[contains(text(),'姓名')])[1]`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
    ],
  },
  {
    name: '弹窗操作',
    id: '3',
    children: [
      {
        id: 'click-ok',
        name: '点击浏览器弹框确定键',
        config:null
      },
      {
        id: 'click-cancel',
        name: '点击浏览器弹框取消键',
        config:null
      },
      {
        id: 'click-text',
        name: '浏览器文本弹框的文本输入',
        config: [
          { paramsName: 'value', paramsValue: `姓名`}
        ],
      },
    ],
  },
  {
    name: '输入操作',
    id: '4',
    children: [
      {
        id: 'web-input',
        name: '输入文本',
        config: [
          { paramsName: 'locator', paramsValue: `xpath=(//input[@placeholder='请输入开始地址'])[1]`},
          { paramsName: 'value', paramsValue: `172.19.5.29`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
      {
        id: 'web-inputdate',
        name: '输入时间',
        config: [
          { paramsName: 'locator', paramsValue: `xpath=(//input[@placeholder='请选择'])[4]`},
          { paramsName: 'key', paramsValue: `HH:mm`},
          { paramsName: 'keys', paramsValue: `-60*4`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
      {
        id: 'web-inputfile',
        name: 'Web端上传文件操作，指定文件目录',
        config: [
          { paramsName: 'locator', paramsValue: `xpath=(//input[@type='file'])[1]`},
          { paramsName: 'localpath', paramsValue: `D:/Jenkins/workspace/Sakura.Web.UI.Automation.Test/TestData/Zip/system_update_9.9.9.zip`},
          { paramsName: 'delete', paramsValue: `false`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
      {
        id: 'web-inputfiles',
        name: 'Web端上传文件操作，默认当前工作目录',
        config: [
          { paramsName: 'locator', paramsValue: `xpath=(//input[@type='file'])[1]`},
          { paramsName: 'catalogue', paramsValue: `user.home`},
          { paramsName: 'localpath', paramsValue: '/Downloads/${manualBackup}'},
          { paramsName: 'delete', paramsValue: `false`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
      {
        id: 'web-inputzs',
        name: 'Web端上传证书操作',
        config: [
          { paramsName: 'device', paramsValue: `AAS_WS`},
          { paramsName: 'locator', paramsValue: `xpath=(//input[@type='file'])[1]`},
          { paramsName: 'catalogue', paramsValue: `user.dir`},
          { paramsName: 'localpath', paramsValue: `/TestData/License/`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
      {
        id: 'web-inputclear',
        name: '输入框清空',
        config: [
          { paramsName: 'locator', paramsValue: `xpath=(//input[@placeholder='请选择'])[4]`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
    ],
  },
  {
    name: '检查操作',
    id: '5',
    children: [
      {
        id: 'web-check',
        name: '检查Web界面元素的文本值',
        config: [
          { paramsName: 'locator', paramsValue: `xpath=(//div[@class='body-content'])[1]`},
          { paramsName: 'expect', paramsValue: `这里填写预期结果`},
          { paramsName: 'message', paramsValue: `这里填写检查失败的提示内容(实际结果和预期结果不一致)`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
      {
        id: 'web-notcheck',
        name: '不检查Web界面元素的文本值',
        config: [
          { paramsName: 'locator', paramsValue: `xpath=(//div[@class='body-content'])[1]`},
          { paramsName: 'expect', paramsValue: `这里填写预期结果`},
          { paramsName: 'message', paramsValue: `这里填写检查失败的提示内容(实际结果和预期结果不一致)`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
      {
        id: 'web-checkvalue',
        name: '检查Web界面元素的属性值',
        config: [
          { paramsName: 'locator', paramsValue: `xpath=(//label[@title='操作步骤'])[1]`},
          { paramsName: 'value', paramsValue: 'title'},
          { paramsName: 'expect', paramsValue: `这里填写预期结果`},
          { paramsName: 'message', paramsValue: `这里填写检查失败的提示内容(实际结果和预期结果不一致)`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
      {
        id: 'web-checkjs',
        name: '检查Web界面调用JS返回的值',
        config: [
          { paramsName: 'locator', paramsValue: `xpath=(//div[@class='body-content'])[1]`},
          { paramsName: 'value', paramsValue: '/Downloads/${manualBackup}'},
          { paramsName: 'expect', paramsValue: `这里填写预期结果`},
          { paramsName: 'message', paramsValue: `这里填写检查失败的提示内容(实际结果和预期结果不一致)`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
      {
        id: 'web-checklist',
        name: '检查从数据库中查询出的结果中的值',
        config: [
          { paramsName: 'details', paramsValue: `condition:Proportion1;subject:xxxx`},
          { paramsName: 'expect', paramsValue: `这里填写预期结果`},
          { paramsName: 'message', paramsValue: `这里填写检查失败的提示内容(实际结果和预期结果不一致)`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
      {
        id: 'web-checkset',
        name: '检查Web本地缓存的list中的元素',
        config: [
          { paramsName: 'value', paramsValue: '${test}'},
          { paramsName: 'expect', paramsValue: '这里填写检查失败的提示内容'},
          { paramsName: 'message', paramsValue: '这里填写检查失败的提示内容(实际结果和预期结果不一致)'},
        ],
      },
      {
        id: 'web-notchecklists',
        name: '不检查Web本地缓存的list中的元素',
        config: [
          { paramsName: 'value', paramsValue: '${test}'},
          { paramsName: 'expect', paramsValue: '这里填写检查失败的提示内容'},
          { paramsName: 'message', paramsValue: '这里填写检查失败的提示内容(实际结果和预期结果不一致)'},
        ],
      },
      {
        id: 'web-fuzzycheck',
        name: '模糊检查Web界面元素的内容',
        config: [
          { paramsName: 'locator', paramsValue: `xpath=(//div[contains(@class,'one-list-title scroll-wrap')])[1]`},
          { paramsName: 'regex', paramsValue: '^(.*(Between :V001 And :V002)).*$'},
          { paramsName: 'message', paramsValue: '审计失败，操作语句错误(实际结果和预期结果不一致)'},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
    ],
  },
  {
    name: '等待操作',
    id: '6',
    children: [
      {
        id: 'wait-forced',
        name: '强制等待',
        config: [
          { paramsName: 'value', paramsValue: `20000`},
        ],
      },
      {
        id: 'web-implicit',
        name: '隐式等待',
        config: [
          { paramsName: 'value', paramsValue: `20000`},
        ],
      },
    ],
  },
  {
    name: '全局变量操作',
    id: '7',
    children: [
      {
        id: 'web-set',
        name: '设置值到全局',
        config: [
          { paramsName: 'locator', paramsValue: `xpath=//[@id='loginFailedNumChart']/div[2]`},
          { paramsName: 'script', paramsValue: `document.getElementsByClassName("input-inner")[0].value`},
          { paramsName: 'regex', paramsValue: `(?<= : )(.?)(?= ()`},
          { paramsName: 'regex', paramsValue: `(?&lt;=共\s)(.*?)(?=\s条)`},
          { paramsName: 'regex', paramsValue: `\d{1,3}\.\d{1,3}\.\d{1,3}\.`},
          { paramsName: 'details', paramsValue: `key:fail`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
      {
        id: 'web-setdate',
        name: '获取日期设置值到全局',
        config: [
          { paramsName: 'key', paramsValue: `获取当天日期时间`},
          { paramsName: 'value', paramsValue: `yyyy-MM-dd`},
          { paramsName: 'key', paramsValue: `获取自定义时间`},
          { paramsName: 'value', paramsValue: `yyyy-MM-dd HH:mm:ss`},
          { paramsName: 'details', paramsValue: `key:today`},
          { paramsName: 'script', paramsValue: `-60*60*24`}
        ],
      },
      {
        id: 'web-setsysinfo',
        name: '获取系统信息设置值到全局',
        config: [
          { paramsName: 'key', paramsValue: `主机IP`},
          { paramsName: 'details', paramsValue: `key:ip`}
        ],
      },
      {
        id: 'web-setusableip',
        name: '获取可用IP设置值到全局',
        config: [
          { paramsName: 'value', paramsValue: `172.19.3.`},
          { paramsName: 'details', paramsValue: `start:1;end:3;key:IP`}
        ],
      },
      {
        id: 'web-setproperties',
        name: '获取配置文件信息',
        config: [
          { paramsName: 'value', paramsValue: `AAS_DBSG_Login_IP`},
          { paramsName: 'details', paramsValue: `key:Agent_IP`}
        ],
      },
      {
        id: 'web-setcalculationformula',
        name: '获取计算公式设置值到全局',
        config: [
          { paramsName: 'key', paramsValue: "${total4}/(${total4}+${total5}+${total6})*100"},
          { paramsName: 'keys', paramsValue: `#.00`},
          { paramsName: 'details', paramsValue: `key:Proportion1`}
        ],
      },
    ],
  },
  {
    name: 'Windows系统操作',
    id: '8',
    children: [
      {
        id: 'windows-keybg',
        name: '模拟键盘普通按键',
        config: [
          { paramsName: 'key', paramsValue: `END`},
        ],
      },
      {
        id: 'windows-keybc',
        name: '模拟键盘组合按键',
        config: [
          { paramsName: 'key', paramsValue: `Ctrl`},
          { paramsName: 'keys', paramsValue: `K`},
        ],
      },
      {
        id: 'windows-skeybc',
        name: '模拟键盘特殊组合按键',
        config: [
          { paramsName: 'key', paramsValue: `Ctrl`},
          { paramsName: 'keys', paramsValue: `Tab`},
        ],
      },
      {
        id: 'windows-skeybcm',
        name: '模拟键盘多重特殊组合按键',
        config: [
          { paramsName: 'key', paramsValue: `Ctrl`},
          { paramsName: 'keys', paramsValue: `Shift`},
          { paramsName: 'value', paramsValue: `K`}
        ],
      },
      {
        id: 'windows-cmd',
        name: '模拟执行CMD命令',
        config: [
          { paramsName: 'value', paramsValue: `D:/King/Eclipse/Sakura.Dmp.Web.Test/Plug-in/AutoIT.exe`}
        ],
      },
    ],
  },
  {
    name: '鼠标操作',
    id: '9',
    children: [
      {
        id: 'mouse-move',
        name: '移动鼠标到指定位置1',
        config: [
          { paramsName: 'details', paramsValue: `x:340;y:410`},
        ],
      },
      {
        id: 'move-byoffset',
        name: '移动鼠标到指定位置2',
        config: [
          { paramsName: 'details', paramsValue: `x:340;y:410`},
        ],
      },
      {
        id: 'move-toelement',
        name: '移动鼠标到指定元素中间',
        config: [
          { paramsName: 'locator', paramsValue: `xpath=(//canvas)[1]`},
          { paramsName: 'state', paramsValue: `today`},
          { paramsName: 'value', paramsValue: `55`},
          { paramsName: 'details', paramsValue: `x:-12;y:0`},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      }
    ],
  },
  {
    name: '文件目录操作',
    id: '10',
    children: [
      {
        id: 'get-file',
        name: '获取本地目录文件，指定文件目录',
        config: [
          { paramsName: 'localpath', paramsValue: "C:/Users/user06/Dowloads"},
          { paramsName: 'details', paramsValue: "key:文件路径丨文件名称丨文件大小;keys:fileName"},
        ],
      },
      {
        id: 'get-files',
        name: '获取本地目录文件，默认当前工作目录',
        config: [
          { paramsName: 'catalogue', paramsValue: `user.home`},
          { paramsName: 'localpath', paramsValue: "/Downloads"},
          { paramsName: 'details', paramsValue: "key:文件路径丨文件名称丨文件大小;keys:fileName"},
        ],
      },
      {
        id: 'delete-file',
        name: '删除本地文件或目录，指定文件目录',
        config: [
          { paramsName: 'localpath', paramsValue: `D:/King/Eclipse/Sakura.Dmp.Web.Test/Plug-in/AutoIT.xml`},
          { paramsName: 'delete', paramsValue: `true`},
        ],
      },
      {
        id: 'delete-files',
        name: '删除本地文件或目录，默认当前工作目录',
        config: [
          { paramsName: 'catalogue', paramsValue: `user.home`},
          { paramsName: 'localpath', paramsValue: "/Downloads/${manualBackup}"},
          { paramsName: 'delete', paramsValue: `true`},
        ],
      },
    ],
  },
  {
    name: '服务器操作',
    id: '11',
    children: [
      {
        id: 'exe-shell',
        name: '连接Linux执行shell命令',
        config: [
          { paramsName: 'device', paramsValue: `AAS_DBSG`},
          { paramsName: 'shell', paramsValue: "rm -rf /home/audit/license/*.lic"}
        ],
      },
      {
        id: 'free-sftp',
        name: '上传本地文件到服务器，指定文件目录',
        config: [
          { paramsName: 'device', paramsValue: `AAS_DBSG`},
          { paramsName: 'localpath', paramsValue: `D:/Jenkins/workspace/Sakura.Web.UI.Automation.Test/TestData/PCAP/`},
          { paramsName: 'filetype', paramsValue: `多个文件`},
          { paramsName: 'filetype', paramsValue: `单个文件`},
          { paramsName: 'value', paramsValue: "sqldbx_10.2.205.2_1972.pcap"},
          { paramsName: 'remotepath', paramsValue: "/sql"},
          { paramsName: 'delete', paramsValue: `false`}
        ],
      },
      {
        id: 'free-sftps',
        name: '上传本地文件到服务器，默认当前工作目录',
        config: [
          { paramsName: 'device', paramsValue: `AAS_DBSG`},
          { paramsName: 'catalogue', paramsValue: "user.dir"},
          { paramsName: 'localpath', paramsValue: `/TestData/PCAP/`},
          { paramsName: 'filetype', paramsValue: `多个文件`},
          { paramsName: 'filetype', paramsValue: `单个文件`},
          { paramsName: 'value', paramsValue: "sqldbx_10.2.205.2_1972.pcap"},
          { paramsName: 'remotepath', paramsValue: "/sql"},
          { paramsName: 'delete', paramsValue: `false`}
        ],
      },
    ],
  },
  {
    name: '数据库操作',
    id: '12',
    children: [
      {
        id: 'db-insertw',
        name: '数据库插入',
        config: [
          { paramsName: 'device', paramsValue: `AAS_DBSG`},
          { paramsName: 'datatype', paramsValue: `Oracle`},
          { paramsName: 'dataenviron', paramsValue: "测试环境"},
          { paramsName: 'port', paramsValue: `1522`},
          { paramsName: 'database', paramsValue: `ORCL`},
          { paramsName: 'sql', paramsValue: `INSERT INTO "TEST"."JDBC" VALUES (1, '小王', 5/3,5/3,sysdate)`},
        ],
      },
      {
        id: 'db-deletew',
        name: '数据库删除',
        config: [
          { paramsName: 'device', paramsValue: `AAS_DBSG`},
          { paramsName: 'datatype', paramsValue: `Oracle`},
          { paramsName: 'dataenviron', paramsValue: "测试环境"},
          { paramsName: 'port', paramsValue: `1522`},
          { paramsName: 'database', paramsValue: `ORCL`},
          { paramsName: 'sql', paramsValue: `DELETE FROM "TEST"."JDBC" WHERE "id"=1`},
        ],
      },
      {
        id: 'db-updatew',
        name: '数据库更新',
        config: [
          { paramsName: 'device', paramsValue: `AAS_DBSG`},
          { paramsName: 'datatype', paramsValue: `Oracle`},
          { paramsName: 'dataenviron', paramsValue: "测试环境"},
          { paramsName: 'port', paramsValue: `1522`},
          { paramsName: 'database', paramsValue: `mysql`},
          { paramsName: 'sql', paramsValue: `UPDATE "TEST"."JDBC" SET "name"='小李' WHERE "id"=2`},
        ],
      },
      {
        id: 'db-queryw',
        name: '数据库查询',
        config: [
          { paramsName: 'device', paramsValue: `AAS_DBSG`},
          { paramsName: 'datatype', paramsValue: `Oracle`},
          { paramsName: 'dataenviron', paramsValue: "测试环境"},
          { paramsName: 'port', paramsValue: `1522`},
          { paramsName: 'database', paramsValue: `mysql`},
          { paramsName: 'sql', paramsValue: `SELECT * FROM "TEST"."JDBC"`},
        ],
      },
      {
        id: 'db-queryws',
        name: '数据库查询，获取结果设置变量',
        config: [
          { paramsName: 'device', paramsValue: `AAS_DBSG`},
          { paramsName: 'datatype', paramsValue: `Oracle`},
          { paramsName: 'dataenviron', paramsValue: "测试环境"},
          { paramsName: 'port', paramsValue: `1522`},
          { paramsName: 'database', paramsValue: `mysql`},
          { paramsName: 'sql', paramsValue: `select * from plugin;`},
          { paramsName: 'details', paramsValue: `key:Proportion1`},
        ],
      },
      {
        id: 'db-procedurew',
        name: '数据库存储过程',
        config: [
          { paramsName: 'device', paramsValue: `AAS_DBSG`},
          { paramsName: 'datatype', paramsValue: `Oracle`},
          { paramsName: 'dataenviron', paramsValue: "测试环境"},
          { paramsName: 'port', paramsValue: `1522`},
          { paramsName: 'database', paramsValue: `ORCL`},
          { paramsName: 'sql', paramsValue: '{call JDBC_TEST(?,?,?,?)}'},
        ],
      },
    ],
  },
  {
    name: '滚动操作',
    id: '13',
    children: [
      {
        id: 'scroll-element',
        name: '滚动到指定元素',
        config: [
          { paramsName: 'locator', paramsValue: `xpath=(//div[@class='body-content'])[1]`},
          { paramsName: 'expect', paramsValue: "文件成"},
          { paramsName: 'skip', paramsValue: `false（默认false，可跳过locator和expect，跳过后会标记为成功）`}
        ],
      },
    ],
  },
]