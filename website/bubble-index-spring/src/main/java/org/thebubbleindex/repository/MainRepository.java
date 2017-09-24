package org.thebubbleindex.repository;

import java.util.List;

import org.thebubbleindex.model.BubbleIndexTimeseries;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MainRepository extends CrudRepository<BubbleIndexTimeseries, Long> {

    @Query("select u from BubbleIndexTimeseries u where u.dtype = ?1 and u.symbol like %?2")
    List<BubbleIndexTimeseries> findBySymbol(final String type, final String symbol);
    
    @Query("select u from BubbleIndexTimeseries u where u.dtype = ?1 and u.name like %?2")
    List<BubbleIndexTimeseries> findByName(final String type, final String name);
    
    @Query("select u from BubbleIndexTimeseries u where u.dtype = ?1")
    List<BubbleIndexTimeseries> findByType(final String type);
    
    @Query("select u from BubbleIndexTimeseries u where u.dtype = ?1 and u.keywords like %?2")
    List<BubbleIndexTimeseries> findByKeywords(final String type, final String keywords);
    
    @Query("select u from BubbleIndexTimeseries u where u.dtype = ?1 and (u.keywords like %?2 or u.keywords like %?3)")
    List<BubbleIndexTimeseries> findByDualKeywords(final String type, final String keyword1, final String keyword2);
    
    @Query("select u from BubbleIndexTimeseries u where u.dtype = ?1 and (u.keywords like %?2 or u.keywords like %?3 or u.keywords like %?4)")
    List<BubbleIndexTimeseries> findByTriKeywords(final String type, final String keyword1, final String keyword2, final String keyword3);
}
