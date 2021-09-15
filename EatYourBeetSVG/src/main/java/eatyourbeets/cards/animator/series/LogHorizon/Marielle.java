package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.List;
import java.util.Map;

public class Marielle extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Marielle.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Marielle()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, 4);

        SetAffinity_Green(1);
        SetAffinity_Orange(2);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return HasSynergy() ? TempHPAttribute.Instance.SetCard(this, true) : super.GetSpecialInfo();
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final Map<CardType, List<AbstractCard>> map = JUtils.Group(p.drawPile.group, c -> c.type);

        int i = 0;
        for (CardType t : map.keySet())
        {
            final CardGroup group = GameUtilities.CreateCardGroup(map.get(t));
            GameActions.Bottom.Motivate(group)
            .AddCallback(i, (index, c) ->
            {
                final float offsetX = (Settings.WIDTH * 0.12f) + index * AbstractCard.IMG_WIDTH * 0.4f;
                final float offsetY = (Settings.HEIGHT * 0.33f) + index * AbstractCard.IMG_HEIGHT * 0.1f;
                if (c != null)
                {
                    GameEffects.TopLevelList.ShowCardBriefly(c.makeStatEquivalentCopy(), offsetX, offsetY);
                }
            });

            i += 1;
        }

        if (info.IsSynergizing)
        {
            GameActions.Bottom.GainTemporaryHP(magicNumber);
            PurgeOnUseOnce();
        }
    }
}