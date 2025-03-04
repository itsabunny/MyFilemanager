package se.kaninis.filemanager.folders;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<FolderEntity, Long> {
}
