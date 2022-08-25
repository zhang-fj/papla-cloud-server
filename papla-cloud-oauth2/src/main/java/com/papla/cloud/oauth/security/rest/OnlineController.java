package com.papla.cloud.oauth.security.rest;

import com.papla.cloud.oauth.security.service.OnlineUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Api(tags = "在线用户")
@RestController
@RequiredArgsConstructor
@RequestMapping("/online")
public class OnlineController {

    private final OnlineUserService onlineUserService;

    @ApiOperation(value = "在线用户", notes = "query")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filter", value = "模糊查询条件"),
            @ApiImplicitParam(name = "page", value = "查询页码", defaultValue = "0"),
            @ApiImplicitParam(name = "size",value = "每页条数", defaultValue = "10")
    })
    @GetMapping
    public ResponseEntity<Object> query(String filter, Pageable pageable) {
        return new ResponseEntity<>(onlineUserService.getAll(filter, pageable), HttpStatus.OK);
    }

    @ApiOperation(value = "强退用户", notes = "kickOut")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "keys", value = "模糊查询条件")
    })
    @DeleteMapping
    public ResponseEntity<Object> kickOut(@RequestBody Set<String> keys){
        for (String key : keys) {
            onlineUserService.kickOut(key);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
