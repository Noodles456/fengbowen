package com.fbw.OneBoot.service;

import com.fbw.OneBoot.dto.NotificationDTO;
import com.fbw.OneBoot.dto.PagDTO;
import com.fbw.OneBoot.enums.NotificationEnum;
import com.fbw.OneBoot.enums.NotificationStatusEnum;
import com.fbw.OneBoot.exception.CustomizeException;
import com.fbw.OneBoot.exception.ErrorCodeImpl;
import com.fbw.OneBoot.mapper.NotificationMapper;
import com.fbw.OneBoot.model.Notification;
import com.fbw.OneBoot.model.NotificationExample;
import com.fbw.OneBoot.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;

    public PagDTO list(Long id, Integer page, Integer size) {
        PagDTO<NotificationDTO> pagDTO = new PagDTO<>();

        NotificationExample notificationExample=new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id);
        Integer totalCount = (int)notificationMapper.countByExample(notificationExample);
        pagDTO.setPagInation(totalCount,page,size);

        if(page<1){
            page=1;
        }if(page>pagDTO.getTotalPage()){
            page=pagDTO.getTotalPage();
        }
        if(size<1){
            size=1;
        }
        if(size>totalCount){
            size=totalCount;
        }
        Integer offsize=size*(page-1);

        NotificationExample example=new NotificationExample();
        example.createCriteria().andReceiverEqualTo(id);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notificationList = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offsize, size));
if(notificationList.size()==0){
    return pagDTO;
}

List<NotificationDTO> notificationDTOS=new ArrayList<>();
for(Notification notification:notificationList){
    NotificationDTO notificationDTO=new NotificationDTO();
    BeanUtils.copyProperties(notification,notificationDTO);
    notificationDTO.setTypeName(NotificationEnum.nameOfType(notification.getType()));
    notificationDTOS.add(notificationDTO);
}
        pagDTO.setData(notificationDTOS);
        return pagDTO;

    }

    public Long unreadCount(Long id) {
        NotificationExample notificationExample=new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id)
        .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Long id, User user) {
Notification notification=notificationMapper.selectByPrimaryKey(id);
if(notification==null){
    throw new CustomizeException(ErrorCodeImpl.READ_NOTIFY);
}
if(!Objects.equals(notification.getReceiver(),user.getId())){
    throw new CustomizeException(ErrorCodeImpl.NOTIFICATION_NOT_FOUND);

}
notification.setStatus(NotificationStatusEnum.READ.getStatus());
notificationMapper.updateByPrimaryKey(notification);
NotificationDTO notificationDTO=new NotificationDTO();
BeanUtils.copyProperties(notification,notificationDTO);
notificationDTO.setTypeName(NotificationEnum.nameOfType(notification.getType()));;
return notificationDTO;
    }
}
