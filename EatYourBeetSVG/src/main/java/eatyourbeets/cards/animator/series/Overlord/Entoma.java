package eatyourbeets.cards.animator.series.Overlord;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Entoma extends AnimatorCard
{
    public static final String ID = Register(Entoma.class.getSimpleName(), EYBCardBadge.Special);

    private static final int ORIGINAL_DAMAGE = 6;
    private static final int ORIGINAL_MAGIC_NUMBER = 3;

    public Entoma()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(ORIGINAL_DAMAGE, 0, ORIGINAL_MAGIC_NUMBER);

        AddExtendedDescription();

        SetUnique(true);
        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect(e -> AbstractDungeon.effectList.add(new BiteEffect(e.hb.cX, e.hb.cY - 40.0F * Settings.scale, Color.SCARLET.cpy())))
        .AddCallback(enemy ->
        {
            if (GameUtilities.TriggerOnKill(enemy, true) && EffectHistory.TryActivateLimited(cardID))
            {
                AbstractDungeon.player.increaseMaxHp(2, false);
                for (AbstractCard c : GameUtilities.GetAllInstances(this))
                {
                    c.upgrade();
                }
            }
        });

        GameActions.Bottom.ApplyPoison(p, m, magicNumber);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        if (PlayerStatistics.getTurnCount() > 0 && this.baseDamage > 0)
        {
            GameActions.Bottom.ModifyAllCombatInstances(uuid, c -> c.baseDamage = Math.max(0, c.baseDamage - 1));
        }
    }

    @Override
    public boolean canUpgrade()
    {
        return true;
    }

    @Override
    public void upgrade()
    {
        this.timesUpgraded += 1;

        this.upgradeDamage(1);

        if (timesUpgraded % 3 == 0)
        {
            upgradeMagicNumber(1);
        }
        this.upgradedMagicNumber = true;

        this.upgraded = true;
        this.name = cardData.strings.NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
    }
}