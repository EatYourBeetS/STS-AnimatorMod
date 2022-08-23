package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class OrbCore_Lightning extends OrbCore
{
    public static final EYBCardData DATA = Register(OrbCore_Lightning.class).SetPower(0, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public static final int VALUE = 9;

    public OrbCore_Lightning()
    {
        super(DATA, Lightning::new, 2);

        Initialize(0, 0, VALUE, 2);
    }

    @Override
    protected AnimatorPower GetPower()
    {
        return new OrbCore_LightningPower(player, 1);
    }

    public static class OrbCore_LightningPower extends OrbCorePower
    {
        public OrbCore_LightningPower(AbstractCreature owner, int amount)
        {
            super(DATA, owner, amount, VALUE);

            updateDescription();
        }

        @Override
        protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
        {
            int[] multiDamage = DamageInfo.createDamageMatrix(value, true);

            GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE");

            ArrayList<AbstractMonster> enemies = AbstractDungeon.getCurrRoom().monsters.monsters;
            for (int i = 0; i < enemies.size(); ++i)
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
}