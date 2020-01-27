package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.BouncingFlaskAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

@SuppressWarnings("SuspiciousNameCombination")
public class Layla extends AnimatorCard
{
    public static final String ID = Register(Layla.class, EYBCardBadge.Special);

    public Layla()
    {
        super(ID, 2, CardRarity.UNCOMMON, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(7, 0, 2, 2);
        SetUpgrade(0, 0, 1, 0);

        SetPiercing(true);
        SetSynergy(Synergies.Chaika);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + (GameUtilities.GetDebuffsCount(mo) * secondaryValue));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT).SetPiercing(true, true);
        GameActions.Bottom.Reload(name, cards ->
        {
            if (cards.size() > 0)
            {
                AbstractPlayer player = AbstractDungeon.player;
                AbstractMonster enemy = GameUtilities.GetRandomEnemy(true);

                if (enemy != null)
                {
                    GameActions.Bottom.VFX(new PotionBounceEffect(player.hb.cY, player.hb.cX, enemy.hb.cX, enemy.hb.cY), 0.3F);
                }

                GameActions.Bottom.Add(new BouncingFlaskAction(enemy, this.magicNumber, cards.size()));
            }
        });
    }
}