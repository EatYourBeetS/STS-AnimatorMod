package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Gluttony extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Gluttony.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.Dark), true));

    public Gluttony()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);

        SetAffinity_Red(1);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        int xValue = GetXValue();
        return xValue > 0 ? TempHPAttribute.Instance.SetCard(this).SetText(GetXValue(), Settings.CREAM_COLOR) : null;
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public int GetXValue() {
        return player != null ? (magicNumber * (player.drawPile.size() - (player.hand.contains(this) ? 1 : 0))) : -1;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ExhaustFromPile(name, secondaryValue, player.drawPile)
                .SetOptions(CardSelection.Top, false)
                .AddCallback(cards -> {
                    for (AbstractCard c : cards) {
                        EYBCard eCard = JUtils.SafeCast(c, EYBCard.class);
                        if (eCard != null && eCard.affinities != null) {
                            CombatStats.Affinities.AddAffinities(eCard.affinities);
                        }
                        GameActions.Bottom.GainTemporaryHP(magicNumber);

                        if (c.type == CardType.CURSE && info.TryActivateLimited()) {
                            GameActions.Bottom.ObtainAffinityToken(Affinity.Dark, upgraded);
                        }
                    }
                });
    }
}