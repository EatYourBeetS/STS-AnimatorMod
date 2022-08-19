package eatyourbeets.cards.animatorClassic.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class Defend extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Defend.class).SetSkill(1, CardRarity.BASIC, EYBCardTarget.None);

    public Defend(String id, int cost, CardTarget target)
    {
        super(GetStaticData(id), id, GR.GetCardImage(DATA.ID + "Alt"), cost, CardType.SKILL, CardColor.COLORLESS,
                CardRarity.BASIC, target);

        //setBannerTexture("images\\cardui\\512\\banner_uncommon.png","images\\cardui\\1024\\banner_uncommon.png");

        this.cropPortrait = false;
        this.tags.add(CardTags.STARTER_DEFEND);
        this.tags.add(GR.Enums.CardTags.IMPROVED_BASIC_CARD);
    }

    public Defend()
    {
        super(DATA);

        Initialize(0, 5);
        SetUpgrade(0, 3);

        this.cropPortrait = false;
        this.tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
    }
}