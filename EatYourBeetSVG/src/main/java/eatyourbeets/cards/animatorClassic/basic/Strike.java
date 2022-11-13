package eatyourbeets.cards.animatorClassic.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.utilities.GameActions;

public class Strike extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Strike.class).SetAttack(1, CardRarity.BASIC);

    public Strike(String id, int cost, CardTarget target)
    {
        super(GetStaticData(id), id, AnimatorResources.GetCardImage(DATA.ID + "Alt"), cost, CardType.ATTACK, CardColor.COLORLESS,
                CardRarity.BASIC, target);

        //setBannerTexture("images\\cardui\\512\\banner_uncommon.png","images\\cardui\\1024\\banner_uncommon.png");

        this.cropPortrait = false;
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTags.STRIKE);
        this.tags.add(GR.Enums.CardTags.IMPROVED_BASIC_CARD);
    }

    public Strike()
    {
        super(DATA);

        Initialize(6, 0);
        SetUpgrade(3, 0);

        this.cropPortrait = false;
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);
    }
}