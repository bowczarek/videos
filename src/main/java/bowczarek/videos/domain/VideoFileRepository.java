package bowczarek.videos.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by bowczarek on 03.06.2017.
 */
@Repository
public interface VideoFileRepository extends JpaRepository<VideoFile, Long> {
    VideoFile findById(@Param("id") long id);
}
