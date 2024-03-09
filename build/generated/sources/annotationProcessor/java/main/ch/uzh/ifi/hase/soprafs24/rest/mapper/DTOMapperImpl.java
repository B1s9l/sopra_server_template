package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-09T17:14:01+0100",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 17.0.10 (Eclipse Adoptium)"
)
public class DTOMapperImpl implements DTOMapper {

    @Override
    public User convertUserPostDTOtoEntity(UserPostDTO userPostDTO) {
        if ( userPostDTO == null ) {
            return null;
        }

        User user = new User();

        user.setPassword( userPostDTO.getPassword() );
        user.setLastIn( userPostDTO.getLastIn() );
        user.setUserId( userPostDTO.getUserId() );
        user.setToken( userPostDTO.getToken() );
        user.setUsername( userPostDTO.getUsername() );

        return user;
    }

    @Override
    public UserGetDTO convertEntityToUserGetDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserGetDTO userGetDTO = new UserGetDTO();

        userGetDTO.setPassword( user.getPassword() );
        userGetDTO.setLastIn( user.getLastIn() );
        userGetDTO.setUserId( user.getUserId() );
        userGetDTO.setToken( user.getToken() );
        userGetDTO.setUsername( user.getUsername() );

        return userGetDTO;
    }
}
