package com.itheima.lucene;


import org.apache.commons.io.FileUtils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class CreatIndex {

    @Test
    public void creatIndex() throws Exception {
        // 创建索引库的写入对象
        // 需要索引库的写入目录 创建索引库的写入目录
        FSDirectory directory = FSDirectory.open(new File("E:\\luceneTest"));
        // 对象的配置信息  需要一个分词器
        // 创建使用的分词器
        //StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_3);
        Analyzer analyzer = new IKAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);

        // 1.读取本地磁盘的所有文件
        File fileDir = new File("E:\\searchsource");
        // 2.获取文件路径下的所有文件
        File[] files = fileDir.listFiles();
        // 3.获取所有文件 打印文件的属性信息
        for (File file : files) {
            System.out.println("文件的名称为======" + file.getName());
            System.out.println("文件的内容为======" + FileUtils.readFileToString(file));
            System.out.println("文件的长度为======" + file.length());
            System.out.println("文件的路径为====" + file.getPath());
            //4.每循环一次,将当前file属性的创建doc文档对象
            Document document = new Document();
            // doc对象存储的是域字段
            // StringFiled存储字符串不分词 可索引
            // TextFiled存储分词 可索引
            // LongFiled数值存储 可索引
            //Field.Store.YES 是否存储
            // 只要数据需要被搜索结果使用,必须yes
            document.add(new TextField("fileName", file.getName(), Field.Store.YES));
            document.add(new TextField("fileContent", FileUtils.readFileToString(file), Field.Store.YES));
            document.add(new LongField("fileSize", file.length(), Field.Store.YES));
            document.add(new StringField("filePath", file.getPath(), Field.Store.YES));
            // 调用写入对象的方法
            indexWriter.addDocument(document);
        }
        // 关闭写入对象,默认提交
        indexWriter.close();

    }

    @Test
    public void updateIndex() throws Exception {
        // 创建索引库的写入对象
        // 需要索引库的写入目录 创建索引库的写入目录
        FSDirectory directory = FSDirectory.open(new File("E:\\luceneTest"));
        // 对象的配置信息  需要一个分词器
        // 创建使用的分词器
        //StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_3);
        Analyzer analyzer = new IKAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);
        // 参数一表示要修改的数据  2表示修改后的数据
        Document document = new Document();
        document.add(new TextField("fileName", "测试修改", Field.Store.YES));
        indexWriter.updateDocument(new Term("fileName", "spring"), document);
        // 关闭写入对象,默认提交
        indexWriter.close();

    }
}
