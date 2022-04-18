package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HiiragiTenri extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(HiiragiTenri.class)
            .SetSkill(4, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.OwariNoSeraph)
            .PostInitialize(data ->
            {
                final AbstractCard token = AffinityToken.GetCard(Affinity.General);
                token.upgrade();
                data.AddPreview(token, true);
            });

    public HiiragiTenri()
    {
        super(DATA);

        Initialize(0, 0, 0, 5);
        SetUpgrade(0, 0, 0, 2);

        SetAffinity_Light(1);
        SetAffinity_Dark(2);

        SetRetainOnce(true);
        SetPurge(true);

        SetAffinityRequirement(Affinity.General, 4);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return magicNumber > 0 ? TempHPAttribute.Instance.SetCard(this, true) : null;
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        GameUtilities.ModifyMagicNumber(this, player.discardPile.size(), false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (magicNumber > 0)
        {
            GameActions.Bottom.GainTemporaryHP(magicNumber);
        }

        if (CheckAffinity(Affinity.General))
        {
            GameActions.Bottom.ObtainAffinityToken(Affinity.General, true);
        }

        GameActions.Bottom.SelectFromPile(name, secondaryValue, p.discardPile)
        .SetFilter(m, (target, c) -> !GameUtilities.IsHindrance(c) && GameUtilities.IsPlayable(c, target))
        .SetOptions(true, true)
        .AddCallback(m, (target, cards) ->
        {
            GameActions.DelayCurrentActions();
            for (AbstractCard c : cards)
            {
               GameActions.Bottom.PlayCard(c, player.discardPile, target).SetExhaust(true);
            }
        });
    }
}