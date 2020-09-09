package com.senderman.stickersorterbot.repository;

import com.senderman.stickersorterbot.model.Sticker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface StickerRepository extends MongoRepository<Sticker, String> {

    Set<Sticker> findAllByUserId(int userId);

    Set<Sticker> findAllByUserIdAndTagsContaining(int userId, Collection<String> tags);

}
