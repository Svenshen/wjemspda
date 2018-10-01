package com.szh.wjemspda.dao;

import com.szh.wjemspda.domain.Mail;

import java.util.List;

public interface MailDao {

    public void add(Mail mail);

    public void delete(Mail mail);

    public void delete(String chepai,int pici);

    public void deleteall();

    public List<Mail> findByChepaiPici(String chepai,int pici);

    public List<Mail> findAll();

    public Mail findByMailnoAndChepaiAndPici(String mailno,String chepai,int pici);

    public long findByChepaiPiciCount(String chepai,int pici);

    public List<Integer> findByChepaidistinctCheci(String chepai);

    public void close();
}
