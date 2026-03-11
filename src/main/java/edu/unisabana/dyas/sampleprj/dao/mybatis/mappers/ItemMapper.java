package edu.unisabana.dyas.sampleprj.dao.mybatis.mappers;


import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import edu.unisabana.dyas.samples.entities.Item;

/**
 *
 * @author cesarvefe
 */
public interface ItemMapper {
    
    
    public List<Item> consultarItems();        
    
    public Item consultarItem(@Param("idit") int id);
    
    public void insertarItem(@Param("item") Item it);

        
}
