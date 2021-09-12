package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.Mathf;

public class Hero extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Hero.class)
            .SetAttack(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.GoblinSlayer);

    public Hero()
    {
        super(DATA);

        Initialize(8, 0, 2);
        SetUpgrade(4, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
        SetAffinity_Light(2, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY)
        .AddCallback(c ->
        {
            if (GameUtilities.IsFatal(c, true) && info.TryActivateLimited())
            {
                final AbstractCard deckInstance = GameUtilities.GetMasterDeckInstance(uuid);
                if (deckInstance == null)
                {
                    return;
                }

                final Random rng = new Random(Settings.seed + (AbstractDungeon.actNum * 17) + (AbstractDungeon.floorNum * 23));
                if (rng.randomBoolean(0.4f + (Mathf.Pow(2, deckInstance.misc) * 0.025f)))
                {
                    final AbstractRelic.RelicTier tier;
                    final int roll = rng.random(0, 99);
                    if (roll < 50)
                    {
                        tier = AbstractRelic.RelicTier.COMMON;
                    }
                    else if (roll < 82)
                    {
                        tier = AbstractRelic.RelicTier.UNCOMMON;
                    }
                    else
                    {
                        tier = AbstractRelic.RelicTier.RARE;
                    }

                    AbstractDungeon.getCurrRoom().addRelicToRewards(tier);
                    deckInstance.misc = 0;
                }
                else
                {
                    deckInstance.misc += 1;
                }
            }
        });
        GameActions.Bottom.Draw(magicNumber);
    }
}