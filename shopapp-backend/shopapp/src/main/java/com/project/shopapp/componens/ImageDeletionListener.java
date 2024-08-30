package com.project.shopapp.componens;

import com.project.shopapp.utils.ImageUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;

@Component
public class ImageDeletionListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCategoryDeletedEvent(CategoryDeletedEvent event) throws IOException {
        // Xóa ảnh sau khi giao dịch hoàn tất
        ImageUtil.deleteImage(event.getThumbnail());
    }
}
