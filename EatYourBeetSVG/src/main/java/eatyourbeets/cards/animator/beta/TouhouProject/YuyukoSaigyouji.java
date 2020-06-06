package eatyourbeets.cards.animator.beta.TouhouProject;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.GraveField;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.KillCharacterAction;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class YuyukoSaigyouji extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(YuyukoSaigyouji.class).SetPower(4, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);;

    public YuyukoSaigyouji()
    {
        super(DATA);

        Initialize(0, 0, 0, 0);
        SetUpgrade(0, 0, 0, 0);
        SetSynergy(Synergies.TouhouProject);
        SetEthereal(true);
        GraveField.grave.set(this, true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ApplyPower(new DeathTouch(p));
    }

    public static class DeathTouch extends AnimatorPower
    {
        public DeathTouch(AbstractCreature owner)
        {
            super(owner, YuyukoSaigyouji.DATA);
            updateDescription();
        }

        public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
            if (damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
                if (target instanceof AbstractMonster) {
                    AbstractMonster mo = (AbstractMonster)target;
                    if (mo.type != AbstractMonster.EnemyType.BOSS) {
                        this.flash();
                        GameActions.Bottom.Add(new KillCharacterAction(mo, mo));
                    }
                }
            }
        }
    }
}