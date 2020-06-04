package eatyourbeets.cards.animator.beta.AngelBeats;

import basemod.ReflectionHacks;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

public class AyatoNaoi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AyatoNaoi.class).SetSkill(3, CardRarity.RARE, EYBCardTarget.None);

    public AyatoNaoi()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetUpgrade(0, 0, 2, 0);

        SetSynergy(Synergies.AngelBeats);
        SetSpellcaster();
        SetExhaust(true);
        CardModifierManager.addModifier(this, new AfterLifeMod());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID);
        for (int i = 0; i < magicNumber; i++) {
            GameActions.Bottom.ChannelOrb(new Dark(), true);
        }
        if (CombatStats.ControlPile.Contains(this)) {
            GameActions.Bottom.Add(new AbstractGameAction() {
                @Override
                public void update() {
                    int totalDamage = 0;
                    for (AbstractMonster mo :AbstractDungeon.getMonsters().monsters) {
                        if (!mo.isDeadOrEscaped()) {
                            if (mo.getIntentBaseDmg() >= 0) {
                                int moDamage = (Integer) ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentDmg");
                                if ((Boolean) ReflectionHacks.getPrivate(mo, AbstractMonster.class, "isMultiDmg")) {
                                    moDamage *= (Integer) ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentMultiAmt");
                                }
                                totalDamage += moDamage;
                            }
                        }
                    }
                    if (totalDamage > 0) {
                        int[] newMultiDamage = new int[AbstractDungeon.getCurrRoom().monsters.monsters.size()];
                        for (int i = 0; i < newMultiDamage.length; i++) {
                            newMultiDamage[i] = totalDamage;
                        }
                        if (Settings.FAST_MODE) {
                            GameActions.Top.Add(new VFXAction(new OfferingEffect(), 0.1F));
                        } else {
                            GameActions.Top.Add(new VFXAction(new OfferingEffect(), 0.5F));
                        }
                        GameActions.Top.Add(new DamageAllEnemiesAction(AbstractDungeon.player, newMultiDamage, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.NONE));
                    }
                    isDone = true;
                }
            });
        }
    }
}