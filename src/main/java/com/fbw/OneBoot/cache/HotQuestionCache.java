package com.fbw.OneBoot.cache;

import com.fbw.OneBoot.dto.HotTagDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class HotQuestionCache {
    private Map<String,Integer> tags=new HashMap<>();
    private List<String> hots=new ArrayList<>();
    public void updateTags(Map<String,Integer> tags){
        int max=5;
        PriorityQueue<HotTagDTO> priorityQueue=new PriorityQueue<>(max);
            tags.forEach((name,priority)->{
                HotTagDTO hotTagDTO=new HotTagDTO();
                hotTagDTO.setName(name);
                hotTagDTO.setPriority(priority);
                if(priorityQueue.size()<max){
                    priorityQueue.add(hotTagDTO);
                }else{
                    HotTagDTO minHot=priorityQueue.peek();
                    if(hotTagDTO.compareTo(minHot)>0){
                        priorityQueue.poll();
                        priorityQueue.add(hotTagDTO);
                    }
                }
        });
        List<String> sortedTags = new ArrayList<>();

        HotTagDTO poll = priorityQueue.poll();
        while (poll != null) {
            sortedTags.add(0, poll.getName());
            poll = priorityQueue.poll();
        }
        hots = sortedTags;
    }

}
