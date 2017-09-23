package org.thebubbleindex.repository;

import java.util.List;

import org.thebubbleindex.model.BubbleIndexTimeseries;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MainRepository extends CrudRepository<BubbleIndexTimeseries, Long> {

    @Query("select u from BubbleIndexTimeseries u where u.dtype = ?1 and u.symbol like %?2")
    List<BubbleIndexTimeseries> findBySymbolIgnoreCase(final String type, final String symbol);
    
    @Query("select u from BubbleIndexTimeseries u where u.dtype = ?1 and u.name like %?2")
    List<BubbleIndexTimeseries> findByNameIgnoreCase(final String type, final String name);
    
    @Query("select u from BubbleIndexTimeseries u where u.dtype = ?1")
    List<BubbleIndexTimeseries> findByTypeIgnoreCase(final String type);
    
    @Query("select u from BubbleIndexTimeseries u where u.dtype = ?1 and u.keywords like %?2")
    List<BubbleIndexTimeseries> findByKeywordsIgnoreCase(final String type, final String keywords);
    
    @Query("select u from BubbleIndexTimeseries u where u.dtype = ?1 and (u.keywords like %?2 or u.keywords like %?3)")
    List<BubbleIndexTimeseries> findByDualKeywordsIgnoreCase(final String type, final String keyword1, final String keyword2);
    
    @Query("select u from BubbleIndexTimeseries u where u.dtype = ?1 and (u.keywords like %?2 or u.keywords like %?3 or u.keywords like %?4)")
    List<BubbleIndexTimeseries> findByTriKeywordsIgnoreCase(final String type, final String keyword1, final String keyword2, final String keyword3);
}
