package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Hero extends AnimatorCard_UltraRare
{
    public static final String ID = Register(Hero.class, EYBCardBadge.Special);

    public Hero()
    {
        super(ID, 1, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(8, 0, 2);
        SetUpgrade(4, 0, 0);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY)
        .AddCallback(c ->
        {
            if (GameUtilities.TriggerOnKill(c, true) && EffectHistory.TryActivateLimited(cardID))
            {
                Random rng = new Random(Settings.seed + (AbstractDungeon.actNum * 17) + (AbstractDungeon.floorNum * 23));
                if (rng.randomBoolean(0.4f))
                {
                    AbstractRelic.RelicTier tier;

                    int roll = rng.random(0, 99);
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
                }
            }
        });
        GameActions.Bottom.Draw(this.magicNumber);
    }
}