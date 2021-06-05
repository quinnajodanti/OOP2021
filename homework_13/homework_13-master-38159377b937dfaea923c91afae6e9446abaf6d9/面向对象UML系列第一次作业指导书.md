# 面向对象UML系列第一次作业指导书

## 注意

**请不要提交官方包代码！！！其代码仅供参考，请使用已经编译打包好的jar文件。**

**因提交官方包源代码导致的任何问题，请自行承担后果！！！**

## 摘要

本次作业，需要完成的任务为实现一个UML类图分析器`UmlInteraction`，学习目标为**UML入门级的理解、UML类图的构成要素及其解析方法**

## 问题

### 基本目标

本次作业最终需要实现一个UML类图分析器，可以通过输入各种指令来进行类图有关信息的查询。

### 基本任务

本次作业的程序主干逻辑（包括解析`mdj`格式的文件为关键数据）我们均已实现，只需要同学们完成剩下的部分。即：**通过实现官方提供的接口，来实现自己的UML分析器**

`UmlInteraction`的**接口定义源代码**都已在接口源代码文件中给出，各位同学需要实现相应的接口，并保证**代码实现功能正确**。

具体来说，各位同学需要新建一个类`MyUmlInteraction`（仅举例，具体类名可自行定义并配置），并实现相应的接口方法。

当然，还需要同学们在主类中调用官方包的`AppRunner`类，并载入自己实现的`MyUmlInteraction`类，来使得程序完整可运行，具体形式下文中有提示。

### 测试模式

本次作业**不设置互测环节**。针对本次作业提交的代码实现，课程将使用公测+bug修复的黑箱测试模式，具体测试规则参见下文。

## 代码结构说明

### UmlInteraction类

`UmlInteraction`的具体接口规格见官方包的jar文件，此处不加赘述。

除此之外，`UmlInteraction`类必须实现一个构造方法

```java
public class MyUmlInteraction implements UmlInteraction {
    public MyUmlInteraction(UmlElement[] elements);
}
```

或者

```java
public class MyUmlInteraction implements UmlInteraction {
    public MyUmlInteraction(UmlElement... elements);
}
```

构造函数的逻辑为将`elements`数组内的各个UML类图元素传入`UmlInteraction`类，以备后续解析。

**请确保构造函数正确实现，且类和构造函数均定义为`public`**。`AppRunner`内将自动获取此构造函数进行`UmlInteraction`实例的生成。

（注：这两个构造方法实际本质上等价，不过后者实际测试使用时的体验会更好，想要了解更多的话可以百度一下，关键词：`Java 变长参数`）

### 交互模式

交互的模式为：

* 测试调用类调用上述构造函数，生成一个实例，同时将UML模型元素传入。
* 之后测试调用类将根据输入输出调用各个接口方法，以实现基于之前传入的UML模型元素的各类查询操作。

## 输入输出

本次作业将会下发`mdj`文件解析工具、UmlInteraction类、输入输出接口（实际上为二合一的工具，接口文档会详细说明）和全局测试调用程序，其中输入输出接口、全局测试程序均在官方接口中。

* 解析工具用于将`mdj`格式文件解析为输入输出接口可以识别的格式，该格式包含了文件内模型中所有关键信息的元素字典表
* 输入输出接口用于对元素字典表的解析和处理、对查询指令的解析和处理以及对输出信息的处理
* 全局测试调用程序会实例化同学们实现的类，并根据输入接口解析内容调用对应方法，并把返回结果通过输出接口进行输出

输入输出接口的具体字符格式已在接口内部定义好，各位同学可以阅读相关代码，这里只给出程序黑箱的字符串输入输出。

### 规则

-   输入一律在标准输入中进行，输出一律向标准输出中输出
-   输入内容以指令的形式输入，一条指令占一行，输出以提示语句的形式输出，一句输出占一行
-   输入使用官方提供的输入接口，输出使用官方提供的输出接口
-   输入的整体格式如下：
    -   由`mdj`文件解析而来的关键元素表
    -   `END_OF_MODEL`分隔开行
    -   指令序列，每条指令一行

### 指令格式一览

各个指令对应的方法名和参数的表示方法详见官方接口说明。

#### 模型中一共有多少个类

输入指令格式：`CLASS_COUNT`

举例：`CLASS_COUNT`

输出：

- `Total class count is x.` x为模型中类的总数

#### 类中的操作有多少个

输入指令格式：`CLASS_OPERATION_COUNT classname`

举例：`CLASS_OPERATION_COUNT Elevator`

输出：

- `Ok, operation count of class "classname" is x.` `x`为类中的操作个数
- `Failed, class "classname" not found.` 类不存在
- `Failed, duplicated class "classname".` 类存在多个

说明：

- 本指令中统计的一律为此类自己定义的操作，不包含继承自其各级父类所定义的操作
- 本指令不检查操作的合法性，所有操作均计入总数。如有重复操作分别计入总数。

#### 类中的属性有多少个

输入指令格式：`CLASS_ATTR_COUNT classname`

举例：`CLASS_ATTR_COUNT Elevator`

输出：

- `Ok, attribute count of class "classname" is x.` `x`为类中属性的个数
- `Failed, class "classname" not found.` 类不存在
- `Failed, duplicated class "classname".` 类存在多个

说明：

- 本指令的查询均需要考虑属性的继承关系，即需包括各级父类定义的属性。

#### 类的操作可见性

输入指令格式：`CLASS_OPERATION_VISIBILITY classname methodname`

举例：`CLASS_OPERATION_VISIBILITY Taxi setStatus`

输出：

- `Ok, operation visibility of method "methodname" in class "classname" is public: xxx, protected: xxx, private: xxx, package-private: xxx.` 该操作的实际可见性统计
- `Failed, class "classname" not found.` 类不存在
- `Failed, duplicated class "classname".` 类存在多个

说明：

- 本指令中统计的一律为此类自己定义的操作，不包含其各级父类所定义的操作
- 在上一条的前提下，需要统计出全部的名为methodname的方法的可见性信息
- 如果不存在对应的方法，则全部置0

#### 类的属性可见性

输入指令格式：`CLASS_ATTR_VISIBILITY classname attrname`

举例：`CLASS_ATTR_VISIBILITY Taxi id`

输出：

- `Ok, attribute "attrname" in class "classname"'s visibility is public/protected/private/package-private.` 该属性的实际可见性
- `Failed, class "classname" not found.` 类不存在
- `Failed, duplicated class "classname".` 类存在多个
- `Failed, attribute "attrname" not found in class "classname".` 类中没有该属性
- `Failed, duplicated attribute "attrname" in class "classname".` 类中属性存在多个同名

说明：

* 本指令的查询均需要考虑属性的继承关系。
* 其中对于父类和子类均存在此名称的属性时，需要按照`duplicated attribute`处理。

#### 类的属性类型

输入指令格式：`CLASS_ATTR_TYPE classname attrname`

举例：`CLASS_ATTR_TYPE Taxi id`

输出：

- `Ok, the type of attribute "attrname" in class "classname" is typeA.` 该属性的类型
- `Failed, class "classname" not found.` 类不存在
- `Failed, duplicated class "classname".` 类存在多个
- `Failed, attribute "attrname" not found in class "classname".` 类中没有该属性
- `Failed, duplicated attribute "attrname" in class "classname".` 类中属性存在多个同名
- `Failed, wrong type of attribute "attrname" in class "classname".` 属性类型错误

说明：

* 类型`type`的数据类型有两种，分别为`ReferenceType`和`NamedType`。对于这两种情形，
  * `NamedType`为命名型类别，只考虑 JAVA 语言八大基本类型(`byte`, `short`, `int`, `long`, `float`, `double`, `char`, `boolean`)和`String`，其余一律视为错误类型。
  * `ReferenceType`为依赖型类别，其指向已定义的类或接口，类型名为对应的类名或接口名。
* 本指令的查询均需要考虑属性的继承关系。
* 其中对于父类和子类均存在此名称的属性时，需要按照`duplicated attribute`处理。

#### 类的操作的参数类型

输入指令格式：`CLASS_OPERATION_PARAM_TYPE classname methodname`

举例：`CLASS_OPERATION_PARAM_TYPE Taxi setStatus`

输出：

- `Ok, method "methodname" in class "classname" has parameter tables and return value: (type1, return: type2), (type3, return: type4), (type5, type6, no return).` 该操作有三种参数表和返回值的搭配，其中
  - 传出列表时可以乱序，内部参数类型也可以乱序，官方接口会自动进行排序（但是**需要编写者自行保证不重不漏**）
  - 如果不存在该命名的操作，则不输出任何搭配。
- `Failed, class "classname" not found.` 类不存在
- `Failed, duplicated class "classname".` 类存在多个
- `Failed, wrong type of parameters or return value in method "methodname" of class "classname".` 存在错误类型
- `Failed, duplicated method "methodname" in class "classname".` 存在重复操作

说明：

- 对于参数类型

  + `NamedType`只考虑JAVA 语言八大基本类型(`byte`, `short`, `int`, `long`, `float`, `double`, `char`, `boolean`)和`String`，其余一律视为错误类型。

  + `ReferenceType`指向已定义的类或接口，类型名为对应的类名或接口名。

- 对于返回值类型

  - `NamedType`只考虑JAVA 语言八大基本类型(`byte`, `short`, `int`, `long`, `float`, `double`, `char`, `boolean`)和`String`，`void`，其余一律视为错误类型。（实际上，`void`也算是一种类型，C/C++/Java对于这件事也都是这样的定义）。`void`不等同于无返回值。

  + `ReferenceType`指向已定义的类或接口，类型名为对应的类名或接口名。

- 参数之间不分次序，即`op(int,int,double)`和`op(double,int,int)`视为具有相同参数类型，但参数和返回值之间是有区别的，且保证最多只有一个返回值，无返回值时相应位置返回`null`。

- 如果两个操作的**操作名相同**，且**参数和返回值的类型也相同**，视为重复操作。

- 如果同时存在错误类型和重复操作两种异常，按错误类型异常处理。

- 本指令中统计的一律为此类自己定义的操作，不包含继承自其各级父类所定义的操作。

#### 类的关联的对端是哪些类

输入指令格式：`CLASS_ASSO_CLASS_LIST classname`

举例：`CLASS_ASSO_CLASS_LIST Elevator`

输出：

- `Ok, associated classes of class "classname" are (A, B, C).` A、B、C为类所有关联的对端的类名，其中
  - 传出列表时可以乱序，官方接口会自动进行排序（但是**需要编写者自行保证不重不漏**；**特别的，对于同名但id不同的类，如果结果同时包含多个的话，需要在列表中返回对应数量个类名**）
  - 如果出现自关联的话，那么自身类也需要加入输出
- `Failed, class "classname" not found.` 类不存在
- `Failed, duplicated class "classname".` 类存在多个

注意：

* 同上一条，Association关系需要考虑父类的继承。即，假如$X$类继承了$Y$类，那么$Y$的Association对端节点也属于$X$。
* 不考虑由属性和操作参数类型引起的关联。

#### 类实现的全部接口

输入指令格式：`CLASS_IMPLEMENT_INTERFACE_LIST classname`

举例：`CLASS_IMPLEMENT_INTERFACE_LIST Taxi`

输出：

- `Ok, implement interfaces of class "classname" are (A, B, C).` A、B、C为继承的各个接口
  - 传出列表时可以乱序，官方接口会自动进行排序（但是需要编写者自行保证不重不漏）
  - 特别值得注意的是，无论是直接实现还是通过父类或者接口继承等方式间接实现，都算做实现了接口
- `Failed, class "classname" not found.` 类不存在
- `Failed, duplicated class "classname".` 类存在多个

#### 类的顶级父类

输入指令格式：`CLASS_TOP_BASE classname`

举例：`CLASS_TOP_BASE AdvancedTaxi`

输出：

- `Ok, top base class of class "classname" is top_classname.`  `top_classname`为顶级父类
- `Failed, class "classname" not found.` 类不存在
- `Failed, duplicated class "classname".` 类存在多个

说明：

- 具体来说，对于类$X$，如果$Y$为其顶级父类的话，则满足
  - $X$是$Y$的子类（此处特别定义，$X$也是$X$的子类）
  - 不存在类$Z$，使得$Y$是$Z$的子类

#### 类是否违背信息隐藏原则

输入指令格式：`CLASS_INFO_HIDDEN classname`

举例：`CLASS_INFO_HIDDEN Taxi`

输出：

- `Yes, information of class "classname" is hidden.` 满足信息隐藏原则。
- `No, attribute xxx in xxx, xxx in xxx, are not hidden.` 不满足信息隐藏原则。
- `Failed, class "classname" not found.` 类不存在
- `Failed, duplicated class "classname".` 类存在多个

说明：

- 信息隐藏原则，指的是**在类属性的定义中，不允许使用private以外的任何可见性修饰**
- 本指令中需要列出全部的非隐藏属性，同时也需要考虑继承自父类的非隐藏属性
- 值得注意的是，父类和子类中，是可以定义同名属性的（甚至还可以不同类型，不同可见性，感兴趣的话可以自己尝试尝试），然而**父类中定义的和子类中定义的实际上并不是同一个属性，需要在输出时进行分别处理**
- 同样的，返回的列表可以乱序，官方接口会进行自动排序（但是依然需要编写者保证不重不漏）

### 样例

#### 样例

输入文本（模型部分导出自`mdj`文件：[传送门](https://gitlab.buaaoo.top/oo_2021_public/guidebook/homework_13)，导出方法见官方包开源文档）

```json
{"_parent":"AAAAAAFF+qBWK6M3Z8Y=","visibility":"public","name":"Door","_type":"UMLClass","_id":"AAAAAAFqpiMge7NXBnk="}
{"_parent":"AAAAAAFqpiMge7NXBnk=","visibility":"public","name":"Door","_type":"UMLOperation","_id":"AAAAAAFqpiQWH7O0bzI="}
{"_parent":"AAAAAAFqpiMge7NXBnk=","visibility":"public","name":"Open","_type":"UMLOperation","_id":"AAAAAAFqpiRcY7O7pzM="}
{"_parent":"AAAAAAFqpiRcY7O7pzM=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqpim3MbPYrBA=","type":"boolean","direction":"return"}
{"_parent":"AAAAAAFqpiRcY7O7pzM=","name":"k","_type":"UMLParameter","_id":"AAAAAAFqpz3cy1dqvuQ=","type":{"$ref":"AAAAAAFqpyZaw1HqYaU="},"direction":"in"}
{"_parent":"AAAAAAFqpiMge7NXBnk=","visibility":"public","name":"Close","_type":"UMLOperation","_id":"AAAAAAFqpyDeZlAA9wA="}
{"_parent":"AAAAAAFqpyDeZlAA9wA=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqpyECbVAHLpo=","type":"boolean","direction":"return"}
{"_parent":"AAAAAAFqpiMge7NXBnk=","visibility":"public","name":"Register","_type":"UMLOperation","_id":"AAAAAAFqpz7UOVfbTr8="}
{"_parent":"AAAAAAFqpz7UOVfbTr8=","name":"e","_type":"UMLParameter","_id":"AAAAAAFqpz83w1gSehs=","type":"","direction":"in"}
{"_parent":"AAAAAAFqpz7UOVfbTr8=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqpz83w1gTXoQ=","type":{"$ref":"AAAAAAFqpyZaw1HqYaU="},"direction":"return"}
{"_parent":"AAAAAAFqpiMge7NXBnk=","visibility":"public","name":"UnRegister","_type":"UMLOperation","_id":"AAAAAAFqpz98b1heYb8="}
{"_parent":"AAAAAAFqpz98b1heYb8=","name":"e","_type":"UMLParameter","_id":"AAAAAAFqpz\/Q61inSCc=","type":"","direction":"in"}
{"_parent":"AAAAAAFqpz98b1heYb8=","name":"k","_type":"UMLParameter","_id":"AAAAAAFqpz\/Q61iokxk=","type":{"$ref":"AAAAAAFqpyZaw1HqYaU="},"direction":"in"}
{"_parent":"AAAAAAFqpz98b1heYb8=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqpz\/Q61ipA8c=","type":"boolean","direction":"return"}
{"_parent":"AAAAAAFqpiMge7NXBnk=","visibility":"public","name":"isOpen","_type":"UMLOperation","_id":"AAAAAAFqwQTh\/MG8LKk="}
{"_parent":"AAAAAAFqwQTh\/MG8LKk=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqwRJTw8PKJ0k=","type":"boolean","direction":"return"}
{"_parent":"AAAAAAFqpiMge7NXBnk=","visibility":"public","name":"getRoomNo","_type":"UMLOperation","_id":"AAAAAAFqwRE8ucKwxBA="}
{"_parent":"AAAAAAFqwRE8ucKwxBA=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqwRHwR8NkxtQ=","type":"int","direction":"return"}
{"_parent":"AAAAAAFqpiMge7NXBnk=","name":null,"_type":"UMLAssociation","end2":"AAAAAAFqpyLHQ1BBCwQ=","end1":"AAAAAAFqpyLHQ1BA8jU=","_id":"AAAAAAFqpyLHQ1A\/uHQ="}
{"reference":"AAAAAAFqyyuTsa1CnU8=","multiplicity":"1","_parent":"AAAAAAFqpyLHQ1A\/uHQ=","visibility":"private","name":"locker","_type":"UMLAssociationEnd","aggregation":"none","_id":"AAAAAAFqpyLHQ1BBCwQ="}
{"reference":"AAAAAAFqpiMge7NXBnk=","multiplicity":"","_parent":"AAAAAAFqpyLHQ1A\/uHQ=","visibility":"public","name":"lockedDoor","_type":"UMLAssociationEnd","aggregation":"none","_id":"AAAAAAFqpyLHQ1BA8jU="}
{"_parent":"AAAAAAFqpiMge7NXBnk=","name":null,"_type":"UMLAssociation","end2":"AAAAAAFqwUWWHPTc\/rg=","end1":"AAAAAAFqwUWWHPTbrlg=","_id":"AAAAAAFqwUWWHPTahS8="}
{"reference":"AAAAAAFqwTWWKvND\/ug=","multiplicity":"1..*","_parent":"AAAAAAFqwUWWHPTahS8=","visibility":"private","name":"client","_type":"UMLAssociationEnd","aggregation":"none","_id":"AAAAAAFqwUWWHPTc\/rg="}
{"reference":"AAAAAAFqpiMge7NXBnk=","multiplicity":"*","_parent":"AAAAAAFqwUWWHPTahS8=","visibility":"private","name":"rooms","_type":"UMLAssociationEnd","aggregation":"none","_id":"AAAAAAFqwUWWHPTbrlg="}
{"_parent":"AAAAAAFqpiMge7NXBnk=","name":null,"_type":"UMLAssociation","end2":"AAAAAAFq4pz3McFqCSQ=","end1":"AAAAAAFq4pz3MMFpo88=","_id":"AAAAAAFq4pz3MMFoTW8="}
{"reference":"AAAAAAFqp0EJi1lLqGo=","multiplicity":"","_parent":"AAAAAAFq4pz3MMFoTW8=","visibility":"public","name":null,"_type":"UMLAssociationEnd","aggregation":"none","_id":"AAAAAAFq4pz3McFqCSQ="}
{"reference":"AAAAAAFqpiMge7NXBnk=","multiplicity":"","_parent":"AAAAAAFq4pz3MMFoTW8=","visibility":"public","name":"sdfdsfgsfdg","_type":"UMLAssociationEnd","aggregation":"none","_id":"AAAAAAFq4pz3MMFpo88="}
{"_parent":"AAAAAAFqpiMge7NXBnk=","visibility":"public","name":"Class1","_type":"UMLClass","_id":"AAAAAAFq6iC1sOB0huU="}
{"_parent":"AAAAAAFqpiMge7NXBnk=","visibility":"private","name":"bOpen","_type":"UMLAttribute","_id":"AAAAAAFqpiN8GLOssfo=","type":"boolean"}
{"_parent":"AAAAAAFqpiMge7NXBnk=","visibility":"private","name":"roomNO","_type":"UMLAttribute","_id":"AAAAAAFqpyGbn1AMoqE=","type":""}
{"_parent":"AAAAAAFqpiMge7NXBnk=","visibility":"private","name":"guests","_type":"UMLAttribute","_id":"AAAAAAFqp0ZAqWCp\/yc=","type":"Vector<Client>"}
{"_parent":"AAAAAAFqpiMge7NXBnk=","visibility":"private","name":"assignedKeys","_type":"UMLAttribute","_id":"AAAAAAFqp0bpg2FufMY=","type":"Vector<Key>"}
{"_parent":"AAAAAAFqpiMge7NXBnk=","visibility":"private","name":"availableKeys","_type":"UMLAttribute","_id":"AAAAAAFqp0frlGIqTHo=","type":"Vector<Key>"}
{"_parent":"AAAAAAFF+qBWK6M3Z8Y=","visibility":"public","name":"Lock","_type":"UMLClass","_id":"AAAAAAFqpyKBqVAUSAo="}
{"_parent":"AAAAAAFqpyKBqVAUSAo=","visibility":"public","name":"lock","_type":"UMLOperation","_id":"AAAAAAFqpyVxfVFaqSg="}
{"_parent":"AAAAAAFqpyVxfVFaqSg=","name":"k","_type":"UMLParameter","_id":"AAAAAAFqpyW721F53Fg=","type":{"$ref":"AAAAAAFqpyZaw1HqYaU="},"direction":"in"}
{"_parent":"AAAAAAFqpyVxfVFaqSg=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqpyW721F6New=","type":"boolean","direction":"return"}
{"_parent":"AAAAAAFqpyKBqVAUSAo=","visibility":"public","name":"unlock","_type":"UMLOperation","_id":"AAAAAAFqpyXW4FGSWdU="}
{"_parent":"AAAAAAFqpyXW4FGSWdU=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqpyYDplGyRh8=","type":"boolean","direction":"return"}
{"_parent":"AAAAAAFqpyKBqVAUSAo=","visibility":"public","name":"match","_type":"UMLOperation","_id":"AAAAAAFqp3wEn26eYK0="}
{"_parent":"AAAAAAFqp3wEn26eYK0=","name":"k","_type":"UMLParameter","_id":"AAAAAAFqp3xbj27tCmE=","type":{"$ref":"AAAAAAFqpyZaw1HqYaU="},"direction":"in"}
{"_parent":"AAAAAAFqp3wEn26eYK0=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqp3xbj27uWUQ=","type":"boolean","direction":"return"}
{"_parent":"AAAAAAFqpyKBqVAUSAo=","visibility":"public","name":"getLockId","_type":"UMLOperation","_id":"AAAAAAFqyPHMP7qoa18="}
{"_parent":"AAAAAAFqyPHMP7qoa18=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqyPJLDbsCkeg=","type":"int","direction":"return"}
{"_parent":"AAAAAAFqpyKBqVAUSAo=","name":null,"_type":"UMLInterfaceRealization","_id":"AAAAAAFqyz3DUrUBj9E=","source":"AAAAAAFqpyKBqVAUSAo=","target":"AAAAAAFqyyuTsa1CnU8="}
{"_parent":"AAAAAAFqpyKBqVAUSAo=","visibility":"private","name":"totalKeys","_type":"UMLAttribute","_id":"AAAAAAFqpyQOxlEmyts=","type":"int"}
{"_parent":"AAAAAAFqpyKBqVAUSAo=","visibility":"private","name":"keys","_type":"UMLAttribute","_id":"AAAAAAFqpyoRiFMTmMs=","type":"Vector<Key>"}
{"_parent":"AAAAAAFqpyKBqVAUSAo=","visibility":"private","name":"lockID","_type":"UMLAttribute","_id":"AAAAAAFqpywyyFPNwW8=","type":"int"}
{"_parent":"AAAAAAFF+qBWK6M3Z8Y=","visibility":"public","name":"Key","_type":"UMLClass","_id":"AAAAAAFqpyZaw1HqYaU="}
{"_parent":"AAAAAAFqpyZaw1HqYaU=","visibility":"public","name":"equals","_type":"UMLOperation","_id":"AAAAAAFqp0vL7mYHuPo="}
{"_parent":"AAAAAAFqp0vL7mYHuPo=","name":"o","_type":"UMLParameter","_id":"AAAAAAFqp0xjgmZWAXk=","type":"Object","direction":"in"}
{"_parent":"AAAAAAFqp0vL7mYHuPo=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqp0xjgmZXPzs=","type":"boolean","direction":"return"}
{"_parent":"AAAAAAFqpyZaw1HqYaU=","visibility":"public","name":"getMatchedLockId","_type":"UMLOperation","_id":"AAAAAAFqp37jkXF7CJ4="}
{"_parent":"AAAAAAFqp37jkXF7CJ4=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqp38tFHHKHMI=","type":"int","direction":"return"}
{"_parent":"AAAAAAFqpyZaw1HqYaU=","visibility":"private","name":"keyID","_type":"UMLAttribute","_id":"AAAAAAFqpyZ7clI8H7g=","type":"int"}
{"_parent":"AAAAAAFqpyZaw1HqYaU=","visibility":"private","name":"matchedLockID","_type":"UMLAttribute","_id":"AAAAAAFqpy7tKFUvHfM=","type":"int"}
{"_parent":"AAAAAAFF+qBWK6M3Z8Y=","visibility":"public","name":"NoMoreKeyException","_type":"UMLClass","_id":"AAAAAAFqp0EJi1lLqGo="}
{"_parent":"AAAAAAFqp0EJi1lLqGo=","name":null,"_type":"UMLGeneralization","_id":"AAAAAAFqp1LTBmtxfV4=","source":"AAAAAAFqp0EJi1lLqGo=","target":"AAAAAAFqp1KmH2r29Ds="}
{"_parent":"AAAAAAFqp0EJi1lLqGo=","name":null,"_type":"UMLAssociation","end2":"AAAAAAFq5htejtC7\/sM=","end1":"AAAAAAFq5htejtC6gxI=","_id":"AAAAAAFq5htejtC5T6Q="}
{"reference":"AAAAAAFqpiMge7NXBnk=","multiplicity":"","_parent":"AAAAAAFq5htejtC5T6Q=","visibility":"public","name":null,"_type":"UMLAssociationEnd","aggregation":"composite","_id":"AAAAAAFq5htejtC7\/sM="}
{"reference":"AAAAAAFqp0EJi1lLqGo=","multiplicity":"","_parent":"AAAAAAFq5htejtC5T6Q=","visibility":"public","name":null,"_type":"UMLAssociationEnd","aggregation":"none","_id":"AAAAAAFq5htejtC6gxI="}
{"_parent":"AAAAAAFqp0EJi1lLqGo=","name":null,"_type":"UMLAssociation","end2":"AAAAAAFq5htsJ9FDz58=","end1":"AAAAAAFq5htsJ9FCuk4=","_id":"AAAAAAFq5htsJ9FBdyU="}
{"reference":"AAAAAAFqyyuTsa1CnU8=","multiplicity":"","_parent":"AAAAAAFq5htsJ9FBdyU=","visibility":"public","name":null,"_type":"UMLAssociationEnd","aggregation":"composite","_id":"AAAAAAFq5htsJ9FDz58="}
{"reference":"AAAAAAFqp0EJi1lLqGo=","multiplicity":"","_parent":"AAAAAAFq5htsJ9FBdyU=","visibility":"public","name":null,"_type":"UMLAssociationEnd","aggregation":"none","_id":"AAAAAAFq5htsJ9FCuk4="}
{"_parent":"AAAAAAFF+qBWK6M3Z8Y=","visibility":"public","name":"Exception","_type":"UMLClass","_id":"AAAAAAFqp1KmH2r29Ds="}
{"_parent":"AAAAAAFF+qBWK6M3Z8Y=","visibility":"public","name":"Client","_type":"UMLClass","_id":"AAAAAAFqwTWWKvND\/ug="}
{"_parent":"AAAAAAFqwTWWKvND\/ug=","visibility":"public","name":"enterRoom","_type":"UMLOperation","_id":"AAAAAAFqwTZbePPJQUA="}
{"_parent":"AAAAAAFqwTZbePPJQUA=","name":"rn","_type":"UMLParameter","_id":"AAAAAAFqwTaykvPsLIM=","type":"int","direction":"in"}
{"_parent":"AAAAAAFqwTZbePPJQUA=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqwTbHdfP1AjM=","type":"boolean","direction":"return"}
{"_parent":"AAAAAAFqwTWWKvND\/ug=","visibility":"public","name":"leaveRoom","_type":"UMLOperation","_id":"AAAAAAFqwUSAY\/Q9Sfs="}
{"_parent":"AAAAAAFqwUSAY\/Q9Sfs=","name":"d","_type":"UMLParameter","_id":"AAAAAAFqwUS7n\/RcqDM=","type":{"$ref":"AAAAAAFqpiMge7NXBnk="},"direction":"in"}
{"_parent":"AAAAAAFqwUSAY\/Q9Sfs=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqwUS7oPRdXXs=","type":"boolean","direction":"return"}
{"_parent":"AAAAAAFqwTWWKvND\/ug=","visibility":"public","name":"locateRoom","_type":"UMLOperation","_id":"AAAAAAFqwUTaWPR1AfU="}
{"_parent":"AAAAAAFqwUTaWPR1AfU=","name":"rn","_type":"UMLParameter","_id":"AAAAAAFqwUUKLfSRkmw=","type":"int","direction":"in"}
{"_parent":"AAAAAAFqwUTaWPR1AfU=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqwUUfk\/SalJI=","type":{"$ref":"AAAAAAFqpiMge7NXBnk="},"direction":"return"}
{"_parent":"AAAAAAFqwTWWKvND\/ug=","name":null,"_type":"UMLAssociation","end2":"AAAAAAFqwUbWV\/aI8Po=","end1":"AAAAAAFqwUbWV\/aHDjw=","_id":"AAAAAAFqwUbWV\/aG5TQ="}
{"reference":"AAAAAAFqpyZaw1HqYaU=","multiplicity":"*","_parent":"AAAAAAFqwUbWV\/aG5TQ=","visibility":"private","name":"keys","_type":"UMLAssociationEnd","aggregation":"none","_id":"AAAAAAFqwUbWV\/aI8Po="}
{"reference":"AAAAAAFqwTWWKvND\/ug=","multiplicity":"","_parent":"AAAAAAFqwUbWV\/aG5TQ=","visibility":"public","name":null,"_type":"UMLAssociationEnd","aggregation":"none","_id":"AAAAAAFqwUbWV\/aHDjw="}
{"_parent":"AAAAAAFqwTWWKvND\/ug=","visibility":"private","name":"clientID","_type":"UMLAttribute","_id":"AAAAAAFqwTXVtfOC318=","type":"int"}
{"_parent":"AAAAAAFF+qBWK6M3Z8Y=","visibility":"public","name":"ElcKey","_type":"UMLClass","_id":"AAAAAAFqyyULIat6fvE="}
{"_parent":"AAAAAAFqyyULIat6fvE=","visibility":"public","name":"equals","_type":"UMLOperation","_id":"AAAAAAFqy0Q7JMCG23I="}
{"_parent":"AAAAAAFqy0Q7JMCG23I=","name":"o","_type":"UMLParameter","_id":"AAAAAAFqy0SKNsDVObs=","type":"Object","direction":"in"}
{"_parent":"AAAAAAFqy0Q7JMCG23I=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqy0SKN8DW850=","type":"boolean","direction":"return"}
{"_parent":"AAAAAAFqyyULIat6fvE=","visibility":"private","name":"sigCode","_type":"UMLAttribute","_id":"AAAAAAFqy0FPcb5DotA=","type":"long"}
{"_parent":"AAAAAAFF+qBWK6M3Z8Y=","visibility":"public","name":"Locker","_type":"UMLInterface","_id":"AAAAAAFqyyuTsa1CnU8="}
{"_parent":"AAAAAAFqyyuTsa1CnU8=","visibility":"public","name":"lock","_type":"UMLOperation","_id":"AAAAAAFqyz66dreg3Oc="}
{"_parent":"AAAAAAFqyz66dreg3Oc=","name":"k","_type":"UMLParameter","_id":"AAAAAAFqyz9BVbhUGOc=","type":{"$ref":"AAAAAAFqpyZaw1HqYaU="},"direction":"in"}
{"_parent":"AAAAAAFqyz66dreg3Oc=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqyz9BVrhV7D8=","type":"boolean","direction":"return"}
{"_parent":"AAAAAAFqyyuTsa1CnU8=","visibility":"public","name":"unlock","_type":"UMLOperation","_id":"AAAAAAFqyz9aIbipNj8="}
{"_parent":"AAAAAAFqyz9aIbipNj8=","name":null,"_type":"UMLParameter","_id":"AAAAAAFqyz+Ga7j4px8=","type":"boolean","direction":"return"}
{"_parent":"AAAAAAFF+qBWK6M3Z8Y=","visibility":"public","name":"Interface1","_type":"UMLInterface","_id":"AAAAAAFq5hWfnsrejMQ="}
{"_parent":"AAAAAAFF+qBWK6M3Z8Y=","visibility":"public","name":"Door","_type":"UMLClass","_id":"AAAAAAFq6i\/M3ODSOBc="}
END_OF_MODEL
CLASS_COUNT
CLASS_OPERATION_COUNT Door
CLASS_ATTR_COUNT Key
CLASS_OPERATION_VISIBILITY Key equals
CLASS_ATTR_VISIBILITY Key keyID
CLASS_ATTR_TYPE Key keyID
CLASS_OPERATION_PARAM_TYPE Key getMatchedLockId
CLASS_ASSO_CLASS_LIST Client
CLASS_IMPLEMENT_INTERFACE_LIST Lock
CLASS_TOP_BASE NoMoreKeyException
CLASS_INFO_HIDDEN Key
```

输出文本

```text
Total class count is 9.
Failed, duplicated class "Door".
Ok, attribute count of class "Key" is 2.
Ok, operation visibility of method "equals" in class "Key" is public: 1, protected: 0, private: 0, package-private: 0.
Ok, attribute "keyID" in class "Key"'s visibility is private.
Ok, the type of attribute "keyID" in class "Key" is int.
Ok, method "getMatchedLockId" in class "Key" has parameter tables and return value: (return: int).
Ok, associated classes of class "Client" are (Door, Key).
Ok, implement interfaces of class "Lock" are (Locker).
Ok, top base class of class "NoMoreKeyException" is Exception.
Yes, information of class "Key" is hidden.
```

## 判定

### 公测（包括弱测、中测与强测）数据基本限制

-   `mdj`文件内容限制
    -   包含且仅包含类图，并在`UMLModel`内进行建模，且每个`UMLModel`内的元素不会引用当前`UMLModel`以外的元素（即关系是一个闭包）
    -   原始mdj文件符合`starUML`规范，可在`starUML`中正常打开和显示
    -   `mdj`文件中最多只包含`300`个元素
    -   **此外为了方便本次的情况处理，保证所建模的类图模型，均可以在Oracle Java 8中正常实现出来**
-   输入指令限制
    -   最多不超过200条指令
    -   输入指令满足标准格式
-   测试数据限制
    - 所有公测数据不会对接口中定义的属性，类属性(static attribute)，类方法(static method)做任何测试要求，本次作业不需要对这些情况进行考虑。

### 测试模式

公测均通过标准输出输出进行。

指令将会通过查询UML类图各种信息的正确性，从而测试UML解析器各个接口的实现正确性。

对于任何满足基本数据限制的输入，程序都应该保证不会异常退出，如果出现问题则视为未通过该测试点。

程序的最大运行cpu时间为`2s`，保证强测数据有一定梯度。

## 提示&说明

-   本次作业中可以自行组织工程结构。任意新增`java`代码文件。只需要保证`UmlInteraction`类的继承与实现即可。
-   **关于本次作业解析器类的设计具体细节，本指导书中均不会进行过多描述，请自行去官方包开源仓库中查看接口的规格，并依据规格进行功能的具体实现**，必要时也可以查看AppRunner的代码实现。关于官方包的使用方法，可以去查看开源库的`README.md`。
-   **如果同时满足多个异常，在查询上层模型发生“异常”后，我们自然不该再去查询这个“异常层次”的下层次模型。**
-   [开源库地址](https://gitlab.buaaoo.top/oo_2021_public/guidebook/homework_13)
-   推荐各位同学在课下测试时使用Junit单元测试来对自己的程序进行测试
    -   Junit是一个单元测试包，**可以通过编写单元测试类和方法，来实现对类和方法实现正确性的快速检查和测试**。还可以查看测试覆盖率以及具体覆盖范围（精确到语句级别），以帮助编程者全面无死角的进行程序功能测试。
    -   Junit已在评测机中部署（版本为Junit4.12，一般情况下确保为Junit4即可），所以项目中可以直接包含单元测试类，在评测机上不会有编译问题。
    -   此外，Junit对主流Java IDE（Idea、eclipse等）均有较为完善的支持，可以自行安装相关插件。推荐两篇博客：
        -   [Idea下配置Junit](https://www.cnblogs.com/wangmingshun/p/6411885.html)
        -   [Idea下Junit的简单使用](https://blog.csdn.net/yitengtongweishi/article/details/80715569)
    -   感兴趣的同学可以自行进行更深入的探索，百度关键字：`Java Junit`。
-   强烈推荐同学们
    -   去阅读本次的源代码
    -   **好好复习下本次的ppt，并理清楚各个`UmlElement`数据模型的结构与关系**。
-   **不要试图通过反射机制来对官方接口进行操作**，我们有办法进行筛查。此外，如果发现有人试图通过反射等手段hack输出接口的话，请邮件  或使用微信向助教进行举报，**经核实后，将直接作为无效作业处理**。

