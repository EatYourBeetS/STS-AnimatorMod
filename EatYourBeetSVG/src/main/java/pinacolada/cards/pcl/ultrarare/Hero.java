package pinacolada.cards.pcl.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.utilities.Mathf;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_UltraRare;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Hero extends PCLCard_UltraRare //TODO
{
    public static final PCLCardData DATA = Register(Hero.class)
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
        SetAffinity_Light(1, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY).forEach(d -> d
        .AddCallback(c ->
        {
            if (PCLGameUtilities.IsFatal(c, true) && info.TryActivateLimited())
            {
                final AbstractCard deckInstance = PCLGameUtilities.GetMasterDeckInstance(uuid);
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
        }));
        PCLActions.Bottom.Draw(magicNumber);
    }
}