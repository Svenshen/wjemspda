package com.szh.wjemspda.dao.impl;

import com.szh.wjemspda.domain.Mail;

import java.util.ArrayList;
import java.util.List;

import com.szh.wjemspda.dao.MailDao;
import io.realm.Realm;
import io.realm.RealmResults;

public class MailDaoImpl implements MailDao {

    Realm realm = null;

    public MailDaoImpl(){
        if(realm == null || realm.isClosed() || realm.isEmpty()){
            realm = Realm.getDefaultInstance();
        }
    }

    @Override
    public void add(final Mail mail) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(mail);
            }
        });
    }

    @Override
    public void delete(final Mail mail) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mail.deleteFromRealm();
            }
        });
    }

    @Override
    public void delete(String chepai, int pici) {
        final RealmResults<Mail> maild = realm.where(Mail.class).equalTo("chepai",chepai).equalTo("pici",pici).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                maild.deleteAllFromRealm();
            }
        });
    }

    @Override
    public void deleteall() {
        final RealmResults<Mail> maild = realm.where(Mail.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                maild.deleteAllFromRealm();
            }
        });
    }

    @Override
    public List<Mail> findByChepaiPici(String chepai, int pici) {
        return realm.where(Mail.class).equalTo("chepai",chepai).equalTo("pici",pici).findAll();
    }

    @Override
    public List<Mail> findAll() {
        return realm.where(Mail.class).findAll();

    }


    @Override
    public Mail findByMailnoAndChepaiAndPici(String mailno, String chepai, int pici) {
        RealmResults<Mail> maild = realm.where(Mail.class).equalTo("mailno",mailno).equalTo("chepai",chepai).equalTo("pici",pici).findAll();
        if(maild.isEmpty()){
            return null;
        }else{
            return maild.get(0);
        }
    }

    @Override
    public long findByChepaiPiciCount(String chepai, int pici) {
        return realm.where(Mail.class).equalTo("chepai",chepai).equalTo("pici",pici).count();

    }

    @Override
    public List<Integer> findByChepaidistinctCheci(String chepai) {
        RealmResults<Mail> maillist = realm.where(Mail.class).distinct("chepai","pici").equalTo("chepai",chepai).findAll();

        ArrayList<Integer> list = new ArrayList<>();
        for(Mail m:maillist){
            list.add(m.getPici());
        }
        return list;
    }

    @Override
    public void close() {
        realm.close();
    }
}
