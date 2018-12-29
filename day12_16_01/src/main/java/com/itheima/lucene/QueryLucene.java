package com.itheima.lucene;


import com.sun.javafx.event.DirectEvent;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class QueryLucene {

    @Test
    public void queryAll() throws Exception {
        // 查询所有的方法
        Query query = new MatchAllDocsQuery();
        doQuery(query);
    }

    @Test
    public void queryByTerm() throws Exception {
        //词条查询
        // term 传入 域字段  和 值
        Query query = new TermQuery(new Term("fileName", "传智播客"));
        doQuery(query);
        doQuery(query);
    }

    @Test
    public void queryByRange() throws Exception {
        //根据范围查询
        // term 传入 文件带下  是否包含边界
        Query query = NumericRangeQuery.newLongRange(
                "fileSize", 1L, 50L,
                true, true);
        doQuery(query);
    }

    @Test
    public void queryByBoolean() throws Exception {
        //组合查询 多个查询组合一起的数据筛选
        // term 传入 文件带下  是否包含边界
        BooleanQuery query = new BooleanQuery();
        Query query1 = new TermQuery(new Term("fileName", "传智播客"));
        Query query2 = new TermQuery(new Term("fileName", "不明觉历"));
        // 将俩个查询对象组合一起
        query.add(query1, BooleanClause.Occur.SHOULD);
        query.add(query2, BooleanClause.Occur.MUST);
        doQuery(query);
    }
    @Test
    public void queryByParser() throws Exception {
         // 解析查询 分词查询
        String searchStr = "升麻是不明觉历的传智播客";
        // 解析对象
        QueryParser parser = new QueryParser("fileName",new IKAnalyzer());
        // 得到query对象
        Query query = parser.parse(searchStr);
        doQuery(query);
    }
    @Test
    public void queryByMultiParser() throws Exception {
        // 解析查询 查询内容
        String searchStr = "升麻是不明觉历的传智播客";
        // 解析对象
        String[] fileds = {"fileName", "fileContent"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fileds, new IKAnalyzer());
        // 得到query对象
        Query query = parser.parse(searchStr);
        doQuery(query);
    }




    private void doQuery(Query query) throws IOException {
        // 创建索引的查询对象 具体查询那些数据
        // 查询 修改 删除
        //创建索引库读取对象,读取之前创建的索引看=库目录
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File("E:\\luceneTest")));
        //创建索引库的搜索对象
        IndexSearcher searcher = new IndexSearcher(reader);
        // 调用search查询对象的方法
        // query为查询的对象  10 为查询的数量
        TopDocs topDocs = searcher.search(query, 10);
        // 获取查询结果的总命中数量
        System.out.println("查询结果的总命中的数量:====" + topDocs.totalHits);
        // 包含每个文档得分和id的数量
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("当前文档的id为====" + scoreDoc.doc);
            System.out.println("当前文档的得分为=====" + scoreDoc.score);
            // 通过文档的id获得每个文档的对象
            Document document = searcher.doc(scoreDoc.doc);
            System.out.println("文件的名称为:=====" + document.get("fileName"));
            System.out.println("文件的内容为:=====" + document.get("fileContent"));
            System.out.println("文件的大小为:=====" + document.get("fileSize"));
            System.out.println("文件的路径为:=====" + document.get("filePath"));
        }
    }
}
