package bowczarek.videos.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bowczarek on 03.06.2017.
 */
@Repository
public interface VideoMediaInfoRepository extends CrudRepository<VideoMediaInfo, Long> {
}
