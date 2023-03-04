# File Transfer

## 项目介绍

> 这是一个基于`BIO`实现的文件传输工具

## 你能够

1. 快速的发送文件到指定的服务器上(默认发送用当前用户的桌面)。
2. 支持断点续传，即使传输过程中出现通讯异常，也能够立即恢复。

## 怎么做

1. 通过调用 Client 的方法进行操作。

## 说明
### 文件传输服务
+ 传输服务默认端口：30088
+ 传输服务默认数据通道端口：5000 - 5555
+ `java -jar transfer-server-xxx.jar 启动传输服务`
### 文件监听插件
+ 指定要监听的文件夹：如 D:\Test
+ 该文件目录结构为：Test\127.0.0.1 || Test\xx.xx.xx.xx(ip address)
+ 监听文件目录的二级目录为要传输的服务地址，会默认将该目录下的文件传输至该服务上

## 使用示例
### 传输客户端
```java
    public static void main(String[] args) {

        FileTransferClient client = null;

        try {
            client = new FileTransferClient("127.0.0.1");

            Boolean isSuccess = client.sendFile(new File("C:\\Users\\Viices Cai\\Downloads\\你不知道的JavaScript（上中下合集） (作者 [美] Kyle Simpson 译者 赵望野 梁杰 单业 姜南) (Z-Library).pdf"), "kali");

            if (isSuccess) {
                System.out.println("传输成功！");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }
```

### 文件监听插件
`java -jar file-listener-xxx.jar d:\\listener.properties 启动文件服务`
+ dirPath=xxxxx(监听目录的文件路径)

## 更新日志
> 2023/03/03
+ 【新增】传输的服务端与客户端
+ 【发布】v1.0.0

> 2023/03/04
+ 【新增】传输插件：文件监听器
+ 【发布】v1.1.0
