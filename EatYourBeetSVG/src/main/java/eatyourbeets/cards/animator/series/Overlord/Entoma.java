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
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Entoma extends AnimatorCard
{
    public static final String ID = Register(Entoma.class, EYBCardBadge.Special);

    public Entoma()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(6, 0, 3);
        SetUpgrade(1, 0, 0);

        AddExtendedDescription();

        SetUnique(true, true);
        SetSynergy(Synergies.Overlord);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 3 == 0)
        {
            upgradeMagicNumber(1);
        }
        this.upgradedMagicNumber = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect(e -> GameEffects.List.Add(new BiteEffect(e.hb.cX, e.hb.cY - 40.0F * Settings.scale, Color.SCARLET.cpy())))
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
}