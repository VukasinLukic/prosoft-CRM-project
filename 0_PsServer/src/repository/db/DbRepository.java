/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository.db;

import repository.Repository;

/**
 *
 * @author Vukasin Lukic
 */
public interface DbRepository<T> extends Repository<T> {

    //default implementacije da bi citljivliji bio kod.
    
    default public void connect() throws Exception {
        DbConnectionFactory.getInstanca().getKonekcija();
    }

    default public void disconnect() throws Exception {
        DbConnectionFactory.getInstanca().getKonekcija().close();
    }

    default public void commit() throws Exception {
        DbConnectionFactory.getInstanca().getKonekcija().commit();
    }

    default public void rollback() throws Exception {
        DbConnectionFactory.getInstanca().getKonekcija().rollback();
    }
}
