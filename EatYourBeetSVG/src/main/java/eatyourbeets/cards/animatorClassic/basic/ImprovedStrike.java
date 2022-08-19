package eatyourbeets.cards.animatorClassic.basic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class ImprovedStrike extends ImprovedBasicCard
{
    public static final EYBCardData DATA = Register(ImprovedStrike.class)
            .SetColor(CardColor.COLORLESS).SetAttack(1, CardRarity.BASIC)
            .SetImagePath(GR.GetCardImage(eatyourbeets.cards.animator.basic.Strike.DATA.ID + "Alt1"));

    public ImprovedStrike()
    {
        super(DATA, GR.GetCardImage(eatyourbeets.cards.animator.basic.Strike.DATA.ID + "Alt2"));

        Initialize(7, 0);
        SetUpgrade(2, 0);

        SetTag(CardTags.STARTER_STRIKE, true);
    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        return super.HasDirectSynergy(other) || other.rarity == CardRarity.COMMON;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
    }
}