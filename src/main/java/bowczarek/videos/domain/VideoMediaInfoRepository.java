package bowczarek.videos.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bowczarek on 03.06.2017.
 */
@Repository
public interface VideoMediaInfoRepository extends PagingAndSortingRepository<VideoMediaInfo, Long> {
}
