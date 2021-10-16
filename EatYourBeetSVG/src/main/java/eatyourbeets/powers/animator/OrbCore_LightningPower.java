package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class OrbCore_LightningPower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_LightningPower.class);

    public OrbCore_LightningPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);

       // this.value = OrbCore_Lightning.VALUE;

        updateDescription();
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        int[] multiDamage = DamageInfo.createDamageMatrix(value, true);

        GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE");

        ArrayList<AbstractMonster> enemies = AbstractDungeon.getCurrRoom().monsters.monsters;
        for(int i = 0; i < enemies.size(); ++i)
        {
            AbstractMonster enemy = enemies.get(i);
            if (!enemy.isDeadOrEscaped())
            {
                GameActions.Bottom.VFX(new LightningEffect(enemy.drawX, enemy.drawY));
                GameActions.Bottom.Add(new DamageAction(enemy, new DamageInfo(p, multiDamage[i], DamageInfo.DamageType.THORNS),
                                                                        AbstractGameAction.AttackEffect.NONE, true));
            }
        }
    }
}