package bowczarek.videos.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by bowczarek on 03.06.2017.
 */
@Repository
public interface VideoFileRepository extends CrudRepository<VideoFile, Long> {

    public VideoFile findById(@Param("id") long id);
}
